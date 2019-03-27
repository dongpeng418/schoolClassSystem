package cn.com.school.classinfo.service;

import cn.com.school.classinfo.model.SysReportConfig;

/**
 * 客户类别service
 *
 * @author dongpp
 * @date 2018/12/13
 */
public interface SysReportConfigService {

    /**
     * 增加客户类别
     *
     * @param sysReportConfig 客户类别
     */
    void add(SysReportConfig sysReportConfig);

    /**
     * 更新客户类别
     *
     * @param sysReportConfig 客户类别
     */
    void update(SysReportConfig sysReportConfig);

    /**
     * 更新系统报告配置
     *
     * @param sysReportConfig 系统报告配置
     */
    void updatePath(SysReportConfig sysReportConfig);

    /**
     * 根据报告类型查询报告配置信息
     *
     * @param reportType 报告类型，1：询价，2：咨询
     * @return 报告配置信息
     */
    SysReportConfig getByReportType(Integer reportType);

    /**
     * 根据ID查询报告配置信息
     *
     * @param id 报告配置ID
     * @return 报告配置信息
     */
    SysReportConfig getById(Integer id);
}
