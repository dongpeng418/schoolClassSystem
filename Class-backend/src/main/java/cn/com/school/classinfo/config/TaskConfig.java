package cn.com.school.classinfo.config;

import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 开启异步、定时任务，配置线程池
 * @author dongpp
 * @date 2018/10/27
 */
@Configuration
@EnableAsync
@EnableScheduling
public class TaskConfig {

    /**
     * SpringBoot2.1可以通过配置文件配置来配置线程池及定时任务相关信息
     * 通过debug级日志发现 taskExecutor 和 taskScheduler 同时配置时会起冲突
     * 解决方式是通过自定义配置加上@Primary注解
     * @param builder TaskExecutorBuilder
     * @return 线程池
     */
    @Primary
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
        return builder.build();
    }

}
