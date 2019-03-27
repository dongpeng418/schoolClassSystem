package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.SysLogQuery;
import cn.com.school.classinfo.model.SysLog;
import com.github.pagehelper.PageInfo;

/**
 * 系统日志服务
 *
 * @author dongpp
 * @date 2018/12/15
 */
public interface SysLogService {

    /**
     * 添加系统日志
     *
     * @param sysLog 系统日志
    */
    void add(SysLog sysLog);

    /**
     * 获取系统日志列表
     *
     * @param query 查询条件
     * @return 日志列表
     */
    PageInfo<SysLog> getListByQuery(SysLogQuery query);
}
