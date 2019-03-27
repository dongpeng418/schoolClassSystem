package cn.com.school.classinfo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author dongpp
 * @date 2018/10/26
 */
public class DateUtil {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取当前日期时间字符串
     *
     * @return 当前日期时间字符串
     */
    public static String nowToString() {
        return new SimpleDateFormat(DATETIME_FORMAT).format(now());
    }
}
