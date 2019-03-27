package cn.com.school.classinfo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 中转接口异常
 *
 * @author dongpp
 * @date 2018/11/14
 */
@Getter
@Setter
@AllArgsConstructor
public class TransferApiException extends RuntimeException {
    private String errorMessage;
}
