package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysWebsiteConfig;
import org.apache.ibatis.annotations.Param;

public interface SysWebsiteConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysWebsiteConfig record);

    int insertSelective(SysWebsiteConfig record);

    SysWebsiteConfig selectByPrimaryKey(Integer id);

    SysWebsiteConfig selectByDomain(@Param("domain") String domain, @Param("type") Integer type);

    SysWebsiteConfig selectByTenantId(@Param("tenantId") Integer tenantId, @Param("type") Integer type);

    int updateByPrimaryKeySelective(SysWebsiteConfig record);

    int updateByPrimaryKey(SysWebsiteConfig record);

    int updatePathByPrimaryKey(SysWebsiteConfig record);
}