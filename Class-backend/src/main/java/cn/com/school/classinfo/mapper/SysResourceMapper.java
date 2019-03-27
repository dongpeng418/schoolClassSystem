/**
 *
 */
package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dongpp
 *
 */
public interface SysResourceMapper {

    SysResource selectBySelective(SysResource sysResource);

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
    List<SysResource> selectUserPermissions(@Param("loginUser") String loginUser);

    /**
     * 获取登录用户所具有的所有权限，并赋给shiro
     * @return
     */
    List<SysResource> selectUserPermissionsByUserType(@Param("loginUser") String loginUser,@Param("userType") int userType);

    /**
     * 获取后台所有需要分配权限的资源列表
     * @return
     */
    List<SysResource> selectNeedDistributeReses();

    /**
     * 获取后台所有需要分配权限的资源列表
     * @return
     */
    List<SysResource> selectNeedDistributeResesByResType(@Param("resType") int resType);


    /**
     * 渠道客户用户给用户分配权限时，只能在自己的权限范围内进行权限分配
     * @return
     */
    List<SysResource> selectCustomerUserCanPermissions(@Param("loginUser") String loginUser,@Param("userType") int userType);

}
