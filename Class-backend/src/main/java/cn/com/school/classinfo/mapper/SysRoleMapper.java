/**
 *
 */
package cn.com.school.classinfo.mapper;

import java.util.List;

import cn.com.school.classinfo.common.query.RoleQuery;
import cn.com.school.classinfo.model.SysRole;
import cn.com.school.classinfo.vo.SysRoleSimpleVO;
import cn.com.school.classinfo.vo.SysRoleVO;

/**
 * @author dongpp
 *
 */
public interface SysRoleMapper {

    SysRole selectBySelective(SysRole sysRole);

    int insert(SysRole sysRole);
    int update(SysRole sysRole);
    int delete(int id);

    SysRole selectRoleInfoByName(SysRole sysRole);

    List<SysRoleVO> selectByQuery(RoleQuery query);

    List<SysRoleSimpleVO> selectAllByCondition(RoleQuery query);

    List<SysRoleSimpleVO> selectByUserId(Integer userId);

    List<SysRoleVO> findRoleListByTenantId(RoleQuery query);

    List<SysRoleSimpleVO> findSimpleRoleListByTenantId(RoleQuery query);

}
