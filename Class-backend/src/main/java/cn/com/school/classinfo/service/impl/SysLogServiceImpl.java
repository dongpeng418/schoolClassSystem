package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.query.SysLogQuery;
import cn.com.school.classinfo.mapper.SysLogMapper;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.service.SysLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统日志服务实现类
 *
 * @author dongpp
 * @date 2018/12/15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogServiceImpl implements SysLogService {

    private final SysLogMapper sysLogMapper;

    public SysLogServiceImpl(SysLogMapper sysLogMapper) {
        this.sysLogMapper = sysLogMapper;
    }

    /**
     * 添加系统日志
     *
     * @param sysLog 系统日志
     */
    @Override
    public void add(SysLog sysLog) {
        sysLogMapper.insert(sysLog);
    }

    /**
     * 获取系统日志列表
     *
     * @param query 查询条件
     * @return 日志列表
     */
    @Override
    public PageInfo<SysLog> getListByQuery(SysLogQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        query.setTenantId(CommonUtil.getTenantId());
        return new PageInfo<>(sysLogMapper.selectListByQuery(query));
    }
}
