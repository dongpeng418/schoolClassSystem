package cn.com.school.classinfo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于系统日志检测字段修改情况
 *
 * @author dongpp
 * @date 2018/12/19
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Modify {

    String value() default "";

    Type type() default Type.CHAR;

    /**
     * 字段类型
     */
    enum Type {
        /**
         * CHAR 字符，IMAGE 图片，CHECKBOX 单选/多选
         */
        CHAR,
        IMAGE,
        CHECKBOX
    }
}
