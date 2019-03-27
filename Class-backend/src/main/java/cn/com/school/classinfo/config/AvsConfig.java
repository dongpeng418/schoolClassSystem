package cn.com.school.classinfo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目配置
 * @author dongpp
 * @date 2018/10/26
 */
@Data
@Component
@ConfigurationProperties(prefix = "avs")
public class AvsConfig {

    /**
     * 中转接口相关配置
     */
    private Api api = new Api();

    /**
     * 路径配置
     */
    private Path path = new Path();

    @Data
    public class Api {
        /**
         * 公司API访问对的APIKEY
         */
        private String apiKey;

        /**
         * API对应的http服务
         */
        private String cityHouseHttpPrefix;

        /**
         * creValue http网址
         */
        private String creValueHttpPrefix;

        /**
         * creValue https网址
         */
        private String creValueHttpsPrefix;

        /**
         * creValue test网址
         */
        private String creValueTestPrefix;

        /**
         * 批量估价开始通知短信模板编码
         */
        private String batchStartNotify;

        /**
         * 批量估价完成通知短信模板编码
         */
        private String batchFinishNotify;

    }

    @Data
    public class Path{
        /**
         * 基路径
         */
        private String base;

        /**
         * 客户公司路径
         */
        private String company;

        /**
         * 报告配置路径
         */
        private String reportConfig;

        /**
         * 批量估价路径
         */
        private String batch;

        /**
         * 临时路径
         */
        private String temp;

        /**
         * 估价报告路径
         */
        private String report;

        /**
         * 网站配置路径
         */
        private String website;
    }
}
