/**
 *
 */
package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysRolePermission;

/**
 * @author dongpp
 * 系统管理模块_角色-权限关联关系表
 *
 */
public interface SysRolePermissionMapper {

    int insert(SysRolePermission sysRolePermission);
    int deleteByRoleId(int id);

}
