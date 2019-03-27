package cn.com.school.classinfo.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Redis工具
 * @author dongpp
 * @date 2018/10/29
 */
public class RedisUtil {

    /**
     * 项目键
     */
    private static final String PROJECT_KEY = "AVS";

    /**
     * 键分隔符
     */
    private static final String KEY_SPLIT = ":";

    /**
     * 获取Redis键名称
     * 建议Key规则：项目名:模块名:功能名[:其它]
     * @param keys 需要组装的Key
     * @return 组装完成的Key
     */
    public static String key(String ...keys){
        if(keys.length == 0){
            throw new IllegalArgumentException("keys is null");
        }
        StringBuilder sb = new StringBuilder(PROJECT_KEY);
        sb.append(KEY_SPLIT);
        for(String key : keys){
            sb.append(key).append(KEY_SPLIT);
        }
        return StringUtils.removeEnd(sb.toString(), KEY_SPLIT);
    }

    /**
     * 获取项目Key
     * @return 项目key
     */
    public static String projectKey(){
        return PROJECT_KEY + KEY_SPLIT;
    }
}
