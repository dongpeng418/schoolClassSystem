package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.mapper.SysReportConfigMapper;
import cn.com.school.classinfo.model.SysReportConfig;
import cn.com.school.classinfo.service.SysReportConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统报告配置服务实现类
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysReportConfigServiceImpl implements SysReportConfigService {

    private final SysReportConfigMapper sysReportConfigMapper;

    public SysReportConfigServiceImpl(SysReportConfigMapper sysReportConfigMapper) {
        this.sysReportConfigMapper = sysReportConfigMapper;
    }

    /**
     * 增加系统报告配置
     *
     * @param sysReportConfig 系统报告配置
     */
    @Override
    public void add(SysReportConfig sysReportConfig) {
        CommonUtil.doEvalCreateUpdateInfo(sysReportConfig, CreateUpdateEnum.CREATE);
        //将之前的报告记录最新字段置为0
        sysReportConfigMapper.updateIsNew(sysReportConfig);
        //增加最新记录
        sysReportConfigMapper.insertSelective(sysReportConfig);
    }

    /**
     * 更新系统报告配置
     *
     * @param sysReportConfig 系统报告配置
     */
    @Override
    public void update(SysReportConfig sysReportConfig) {
        CommonUtil.doEvalCreateUpdateInfo(sysReportConfig, CreateUpdateEnum.UPDATE);
        sysReportConfigMapper.updateByPrimaryKeySelective(sysReportConfig);
    }

    /**
     * 更新系统报告配置
     *
     * @param sysReportConfig 系统报告配置
     */
    @Override
    public void updatePath(SysReportConfig sysReportConfig) {
        CommonUtil.doEvalCreateUpdateInfo(sysReportConfig, CreateUpdateEnum.UPDATE);
        sysReportConfigMapper.updatePath(sysReportConfig);
    }

    /**
     * 根据报告类型查询报告配置信息
     *
     * @param reportType 报告类型，1：询价，2：咨询
     * @return 报告配置信息
     */
    @Override
    public SysReportConfig getByReportType(Integer reportType) {
        return sysReportConfigMapper.selectByReportType(reportType, CommonUtil.getTenantId());
    }

    /**
     * 根据ID查询报告配置信息
     *
     * @param id 报告配置ID
     * @return 报告配置信息
     */
    @Override
    public SysReportConfig getById(Integer id) {
        return sysReportConfigMapper.selectByPrimaryKey(id);
    }
}
