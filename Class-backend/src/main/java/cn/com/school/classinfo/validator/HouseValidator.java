package cn.com.school.classinfo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HouseValidatorHandler.class)
public @interface HouseValidator {

    String message() default "房屋信息参数异常";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
