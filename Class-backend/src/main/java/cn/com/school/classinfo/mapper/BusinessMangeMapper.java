package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysBusinessManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务管理Mapper
 *
 * @author dongpp
 * @date 2018/11/21
 */
public interface BusinessMangeMapper {

    /**
     * 插入业务管理
     *
     * @param businessManage 业务管理
     * @return 数量
     */
    int insert(SysBusinessManage businessManage);

    /**
     * 更新业务管理信息
     *
     * @param businessManage 业务管理
     * @return 数量
     */
    int update(SysBusinessManage businessManage);

    /**
     * 删除业务管理信息
     *
     * @param businessManage 业务管理
     * @return 数量
     */
    int delete(SysBusinessManage businessManage);

    /**
     * 查询列表
     *
     * @return 列表
     */
    List<SysBusinessManage> selectByCompanyId(Integer companyId);

    /**
     * 查询列表
     *
     * @param ids ID数组
     * @return 列表
     */
    List<SysBusinessManage> selectByIds(@Param("ids") String[] ids);

    /**
     * 查询列表
     *
     * @param manage 业务类型
     * @return 列表
     */
    SysBusinessManage selectByName(SysBusinessManage manage);
}
