/**
 *
 */
package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysUserRole;

/**
 * @author dongpp
 * 系统权限管理_用户角色关联关系表
 */
public interface SysUserRoleMapper {
    int insert(SysUserRole sysUserRole);
    int update(SysUserRole sysUserRole);
    int delete(SysUserRole sysUserRole);
    int deleteByUserId(Integer deleteByUserId);
}
