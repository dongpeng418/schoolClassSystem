package cn.com.school.classinfo.exception;

import cn.com.school.classinfo.common.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理
 * @author dongpp
 * @date 2018/10/22
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 中转接口异常处理
     *
     * @param e 异常
     * @return 响应消息
     */
    @ExceptionHandler(value = TransferApiException.class)
    public ResultMessage transferApiExceptionHandler(TransferApiException e) {
        return ResultMessage.apiError(e.getErrorMessage());
    }

    /**
     * 缺失参数异常处理
     *
     * @param e 异常
     * @return 响应消息
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultMessage missParamExceptionHandler(MissingServletRequestParameterException e) {
        return ResultMessage.paramError(e.getParameterName());
    }

    /**
     * 用户权限异常
     *
     * @param e 异常
     * @return 响应消息
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public ResultMessage authExceptionHandler(AuthenticationException e) {
        return ResultMessage.authError(e.getMessage());
    }

    /**
     * 验证接口参数失败异常
     * @param e 失败异常对象
     * @return 错误消息
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultMessage validErrorHandler(ConstraintViolationException e){
        Set<ConstraintViolation<?>> errorList = e.getConstraintViolations();
        StringBuilder errorInfo = new StringBuilder();
        if(CollectionUtils.isNotEmpty(errorList)){
            errorList.forEach(constraintViolation -> errorInfo.append(constraintViolation.getMessage()).append(","));
        }
        String errorMsg = StringUtils.removeEnd(errorInfo.toString(), ",");
        return ResultMessage.requestError(errorMsg);
    }

    /**
     * 验证接口参数失败异常
     * @param e 失败异常对象
     * @return 错误消息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultMessage validErrorHandler(MethodArgumentNotValidException e){
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        return commonValidErrorHandler(errorList);
    }

    /**
     * 验证接口参数失败异常
     * @param e 失败异常对象
     * @return 错误消息
     */
    @ExceptionHandler(value = BindException.class)
    public ResultMessage bindErrorHandler(BindException e){
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        return commonValidErrorHandler(errorList);
    }

    /**
     * 验证失败通用异常处理
     *
     * @param errorList 异常列表
     * @return 错误消息
     */
    private ResultMessage commonValidErrorHandler(List<ObjectError> errorList){
        StringBuilder errorInfo = new StringBuilder();
        if(CollectionUtils.isNotEmpty(errorList)){
            errorList.forEach(objectError -> errorInfo.append(objectError.getDefaultMessage()).append(","));
        }
        String errorMsg = StringUtils.removeEnd(errorInfo.toString(), ",");
        return ResultMessage.requestError(errorMsg);
    }


    /**
     * 其它异常处理
     * @param e 异常
     * @return 响应消息
     */
    @ExceptionHandler(value = Exception.class)
    public ResultMessage exceptionHandler(Exception e){
        log.error("controller error: ", e);
        return ResultMessage.serverError("server error");
    }
}
