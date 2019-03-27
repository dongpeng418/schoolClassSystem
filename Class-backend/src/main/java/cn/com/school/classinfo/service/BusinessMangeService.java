package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.BaseQuery;
import cn.com.school.classinfo.model.SysBusinessManage;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 业务管理Servcie
 *
 * @author dongpp
 * @date 2018/11/21
 */
public interface BusinessMangeService {

    /**
     * 增加业务管理信息
     *
     * @param manage 业务管理信息
     */
    void add(SysBusinessManage manage);

    /**
     * 保存业务管理信息
     *
     * @param manage 业务管理信息
     */
    void save(SysBusinessManage manage);

    /**
     * 删除业务管理信息
     *
     * @param manage 业务管理信息
     */
    void delete(SysBusinessManage manage);

    /**
     * 根据名称获取业务管理信息
     *
     * @param name 业务名称
     */
    SysBusinessManage getByName(String name);

    /**
     * 根据多个ID获取业务管理列表
     *
     * @param ids id数组
     * @return 业务管理列表
     */
    List<SysBusinessManage> getByIds(String[] ids);

    /**
     * 获取业务管理信息列表
     *
     * @return 列表
     */
    List<SysBusinessManage> list();

    /**
     * 分页获取业务管理信息列表
     *
     * @param query 查询条件
     * @return 列表
     */
    PageInfo<SysBusinessManage> pageable(BaseQuery query);
}
