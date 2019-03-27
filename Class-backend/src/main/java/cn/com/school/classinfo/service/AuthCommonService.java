/**
 *
 */
package cn.com.school.classinfo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;

import cn.com.school.classinfo.common.query.RoleQuery;
import cn.com.school.classinfo.common.query.UserQuery;
import cn.com.school.classinfo.model.SysOrganization;
import cn.com.school.classinfo.model.SysPermission;
import cn.com.school.classinfo.model.SysResource;
import cn.com.school.classinfo.model.SysRole;
import cn.com.school.classinfo.model.SysTenant;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.vo.SysOrganizationVO;
import cn.com.school.classinfo.vo.SysRoleSimpleVO;
import cn.com.school.classinfo.vo.SysRoleVO;
import cn.com.school.classinfo.vo.SysUserVO;

/**
 * @author dongpp
 * 权限管理通用接口
 *
 */
public interface AuthCommonService {

    /**
     * 获取所有需要经过授权的资源列表，配置到shiro拦截器中,
     * 配置方式：  ShiroConfig.ShiroFilterFactoryBean
     * 拦截方式: JWTFilter
     *
     * @return
     */
    List<SysResource> selectNeedAuthcs();

    /**
     * 获取登录用户所具有的所有权限，并赋给shiro
     * @return
     */
    List<SysResource> selectUserPermissions(String loginUser);

    int insertSysUser(SysUser sysUser);

    /**
     * 通过用户登录账户，或者用户信息
     * @param loginUser
     * @return SysUser
     */
    SysUser findUserByLoginUser(String loginUser);

    SysUser findUserById(int id) ;

    PageInfo<SysUserVO> findUserList(UserQuery query) ;

    int updateSysUserStatusById(int id,int status) ;

    int updateByLoginUserAndCId(SysUser sysUser);


    SysOrganization findOrgInfoByName(String name);
    int insertOrgInfo(SysOrganization sysOrganization);
    int updateOrgInfo(SysOrganization sysOrganization);
    SysOrganization findOrgInfoById(int id);

    Integer findOrgPriority(Integer parentId);
    /**
     * 递归获取下级所有机构
     * @return
     */
    List<SysOrganizationVO> findUserManagedOrgs();

    boolean addUserRoleInfo(SysRole sysRole);

    boolean editUserRoleInfo(SysRole sysRole);

    SysRole findRoleInfoByName(SysRole sysRole);

    SysRole findRoleInfoById(int id);

    PageInfo<SysRoleVO> findRoleList(RoleQuery query);

    List<SysRoleSimpleVO> findAllRoleByCondition(RoleQuery query);

    List<SysRoleSimpleVO> findRoleByUserId(Integer userId);

    PageInfo<SysRoleVO> findRoleListByTenantId(RoleQuery query);

    List<SysRoleSimpleVO> findRoleListByTenantIdNoPage(RoleQuery query);

    boolean deleteRoleInfoById(int id);

    boolean distributionUserRole(HttpServletRequest request,int userId, List<Integer> roleIds);

    /**
     * @param sysOrganization
     * @return
     */
    boolean changeOrgInfoStatus(SysOrganization sysOrganization);

    List<SysResource> selectNeedDistributeReses();

    List<SysPermission> selectPermissionsByRoleId(int roleId);

    /**
     * @param resType
     * @return
     */
    List<SysResource> selectNeedDistributeReses(int resType);

    /**
     * @param loginUser
     * @param userType
     * @return
     */
    SysUser findUserByLoginUser(String loginUser, int userType);

    /**
     * @param loginUser
     * @param userType
     * @return
     */
    List<SysResource> selectUserPermissionsByUserType(String loginUser, int userType);

    /**
     * @param loginUser
     * @param customerCompanyId
     * @return
     */
    SysUser findUserByLoginUserAndCompanyId(String loginUser, int customerCompanyId);

    /**
     * @param sysUser
     * @return
     */
    int updateTenantSysUser(SysUser sysUser);

    /**
     * 看该客户公司下是否已经有客户账户
     * @param userType
     * @param customerCompanyId
     * @return
     */
    List<SysUser> findUserByCIdAndUserType(int userType, int customerCompanyId);

    /**
     * 渠道用户列表
     * @param query
     * @return
     */
    PageInfo<SysUserVO> findTenantUserList(UserQuery query);

    /**
     * 渠道客户用户列表
     * @param query
     * @return
     */
    PageInfo<SysUserVO> findCustomerUserList(UserQuery query);

    /**
     * @return
     */
    List<SysResource> selectCustomerUserCanPermissions(String loginUser,int resType);

    /**
     * 通过用户登录名、用户类型、渠道ID得到用户信息
     * @param loginUser
     * @param userType
     * @param tenantId
     * @return
     */
    SysUser findUserByLoginUserAndUTypeAndTId(String loginUser, int userType, int tenantId);

    SysTenant selectByDomain(SysTenant param);

    /**
     * @param id
     * @return
     */
    SysUser findAllStatusUserById(int id);

}
