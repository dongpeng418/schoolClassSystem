package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysReportConfig;
import org.apache.ibatis.annotations.Param;

public interface SysReportConfigMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysReportConfig record);

    int insertSelective(SysReportConfig record);

    SysReportConfig selectByPrimaryKey(Integer id);

    SysReportConfig selectByReportType(@Param("reportType") Integer reportType, @Param("tenantId") Integer tenantId);

    int updateByPrimaryKeySelective(SysReportConfig record);

    int updatePath(SysReportConfig record);

    int updateIsNew(SysReportConfig record);

    int updateByPrimaryKey(SysReportConfig record);
}