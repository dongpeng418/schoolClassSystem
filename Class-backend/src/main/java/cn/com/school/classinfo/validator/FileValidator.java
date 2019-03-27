package cn.com.school.classinfo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidatorHandler.class)
public @interface FileValidator {

    int max();

    String message() default "上传文件大小超过限制";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
