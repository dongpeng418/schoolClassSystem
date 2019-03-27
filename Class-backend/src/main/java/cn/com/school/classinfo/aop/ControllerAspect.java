package cn.com.school.classinfo.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 对接口性能进行监控
 * 
 * @author hanpt
 *
 */
@Aspect
@Component
@Slf4j
public class ControllerAspect {

    /**
     * AOP的切入点
     */
    @Pointcut("execution(* cn.com.school.classinfo.controller.*.*(..))")
    public void aspect() {}

    /**
     * 性能监控
     * 
     * @param proceedingJoinPoint
     * @return 接口返回信息
     * @throws Throwable
     */
    @Around("aspect()")
    public Object logTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            if(!proceedingJoinPoint.toShortString().contains("handleUnfinishedTask")){
                long ts = (System.currentTimeMillis() - begin);
                log.info("{}, spend time:{}", proceedingJoinPoint.toShortString(), ts);
                Object[] args = proceedingJoinPoint.getArgs();
                if(ArrayUtils.isNotEmpty(args) && args.length > 0){
                    StringBuilder params = new StringBuilder();
                    for(Object arg : args){
                        if(Objects.isNull(arg)){
                            continue;
                        }
                        params.append(arg.toString()).append(",");
                    }
                    log.info("parameters: {}", params.toString());
                }
            }
        }
    }
}
