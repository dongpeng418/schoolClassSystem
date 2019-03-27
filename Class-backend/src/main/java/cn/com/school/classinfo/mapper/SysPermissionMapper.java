/**
 *
 */
package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dongpp
 *
 */
public interface SysPermissionMapper {

    List<SysPermission> selectPermissionsByRoleId(@Param("roleId") int roleId);

}
