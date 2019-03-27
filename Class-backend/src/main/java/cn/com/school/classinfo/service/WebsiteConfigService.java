package cn.com.school.classinfo.service;

import cn.com.school.classinfo.model.SysWebsiteConfig;
import cn.com.school.classinfo.vo.SysWebsiteConfigVO;

/**
 * 网站配置Servcie
 *
 * @author dongpp
 * @date 2018/11/22
 */
public interface WebsiteConfigService {

    /**
     * 保存网站配置信息
     *
     * @param configVO 配置信息VO
     */
    void save(SysWebsiteConfigVO configVO);
    /**
     * 增加网站配置信息
     *
     * @param config 配置信息
     */
    void add(SysWebsiteConfig config);

    /**
     * 更新网站配置路径信息
     *
     * @param config 配置信息
     */
    void updatePath(SysWebsiteConfig config);

    /**
     * 根据域名获取网站配置信息
     *
     * @param domain 域名
     * @param type 配置类型，1：渠道，2：客户
     * @return 配置信息
     */
    SysWebsiteConfig getByDomain(String domain, Integer type);

    /**
     * 根据域名获取网站配置信息
     *
     * @param tenantId 域名
     * @param type 配置类型，1：渠道，2：客户
     * @return 配置信息
     */
    SysWebsiteConfig getByTenantId(Integer tenantId, Integer type);
}
