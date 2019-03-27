/**
 *
 */
package cn.com.school.classinfo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.school.classinfo.common.query.UserQuery;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.vo.SysUserVO;

/**
 * @author dongpp
 *
 */
public interface SysUserMapper {

    SysUser selectById(@Param("id") int id);

    SysUser selectAllStatusUserById(@Param("id") int id);

    int insert(SysUser sysUser);

    int updateByLoginUser(SysUser sysUser);

    int updateStatusById(@Param("id") int id,@Param("status") int status);

    int updateStatusByOrgId(@Param("status") int status,@Param("organizationId") int organizationId);

    SysUser findUserByLoginUser(@Param("loginUser") String loginUser);

    SysUser findUserByLoginUserAndUserType(@Param("loginUser") String loginUser,@Param("userType") int userType);

    SysUser findUserByLoginUserAndUTypeAndTId(@Param("loginUser") String loginUser,@Param("userType") int userType,@Param("tenantId") int tenantId);

    SysUser findUserByLoginUserAndCId(@Param("loginUser") String loginUser,@Param("customerCompanyId") int customerCompanyId);

    List<SysUser> findUserByCIdAndUserType(@Param("userType") int userType,@Param("customerCompanyId") int customerCompanyId);

    /**
     * 更新用户，通过用户登录ID+客户公司ID
     * @param sysUser
     * @return
     */
    int updateByLoginUserAndCId(SysUser sysUser);

    /**
     * 用户列表
     * @param query
     * @return
     */
    List<SysUserVO> selectByQuery(UserQuery query);

    /**
     * 渠道用户列表
     * @param query
     * @return
     */
    List<SysUserVO> selectTenantUsersByQuery(UserQuery query);

    /**
     * 渠道客户用户列表
     * @param query
     * @return
     */
    List<SysUserVO> selectCustomerUsersByQuery(UserQuery query);

}
