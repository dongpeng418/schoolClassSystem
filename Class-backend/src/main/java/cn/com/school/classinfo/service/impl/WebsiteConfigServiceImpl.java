package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.mapper.SysWebsiteConfigMapper;
import cn.com.school.classinfo.model.SysWebsiteConfig;
import cn.com.school.classinfo.service.WebsiteConfigService;
import cn.com.school.classinfo.vo.SysWebsiteConfigVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 网站配置服务实现类
 *
 * @author dongpp
 * @date 2018/11/22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WebsiteConfigServiceImpl implements WebsiteConfigService {

    private final SysWebsiteConfigMapper websiteConfigMapper;

    public WebsiteConfigServiceImpl(SysWebsiteConfigMapper websiteConfigMapper) {
        this.websiteConfigMapper = websiteConfigMapper;
    }

    /**
     * 保存网站配置信息
     *
     * @param configVO 配置信息VO
     */
    @Override
    public void save(SysWebsiteConfigVO configVO) {
        SysWebsiteConfig config = new SysWebsiteConfig();
        BeanUtils.copyProperties(configVO, config);
        websiteConfigMapper.updateByPrimaryKey(config);
    }
    /**
     * 增加网站配置信息
     *
     * @param config 配置信息
     */
    @Override
    public void add(SysWebsiteConfig config) {
        websiteConfigMapper.insert(config);
    }

    /**
     * 更新网站配置路径信息
     *
     * @param config 配置信息
     */
    @Override
    public void updatePath(SysWebsiteConfig config) {
        websiteConfigMapper.updatePathByPrimaryKey(config);
    }

    /**
     * 根据域名获取网站配置信息
     *
     * @param domain 域名
     * @param type 配置类型，1：渠道，2：客户
     * @return 配置信息
     */
    @Override
    public SysWebsiteConfig getByDomain(String domain, Integer type) {
        return websiteConfigMapper.selectByDomain(domain, type);
    }

    /**
     * 根据域名获取网站配置信息
     *
     * @param tenantId 域名
     * @param type 配置类型，1：渠道，2：客户
     * @return 配置信息
     */
    @Override
    public SysWebsiteConfig getByTenantId(Integer tenantId, Integer type){
        return websiteConfigMapper.selectByTenantId(tenantId, type);
    }
}
