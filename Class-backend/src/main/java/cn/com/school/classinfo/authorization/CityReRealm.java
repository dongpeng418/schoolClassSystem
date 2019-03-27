package cn.com.school.classinfo.authorization;

import cn.com.school.classinfo.model.SysResource;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.utils.JWTUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CityReRealm extends AuthorizingRealm {

    private AuthCommonService authCommonService;

    @Autowired
    public void setUserService(AuthCommonService authCommonService) {
        this.authCommonService = authCommonService;
    }

    public CityReRealm() {
        this.setCredentialsMatcher(new JWTCredentialsMatcher());
    }

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser currentUser = (SysUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        int userType = currentUser.getUserType();
        List<SysResource> resources = authCommonService.selectUserPermissionsByUserType(currentUser.getLoginUser(),userType);
        Set<String> permissions = new HashSet<>();
        for (SysResource sysResource : resources) {
            permissions.add(sysResource.getCode());
        }
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String loginUser = JWTUtil.getUsername(token);
        String userType = JWTUtil.getUserType(token);
        String customerCompanyId = JWTUtil.getCustomerCompanyId(token);
        if (loginUser == null) {
            throw new AuthenticationException("token invalid");
        }

        SysUser dbUser = null;
        if(!Strings.isNullOrEmpty(userType)) {
            if(userType.equals("1")) {//渠道用户
                String tenantId = JWTUtil.getTenantId(token);
                dbUser = authCommonService.findUserByLoginUserAndUTypeAndTId(loginUser,1,Integer.parseInt(tenantId));
            }else {//客户用户或者渠道客户用户
                if(Strings.isNullOrEmpty(customerCompanyId)) {
                    throw new AuthenticationException("username or password error");
                }
                dbUser = authCommonService.findUserByLoginUserAndCompanyId(loginUser,Integer.parseInt(customerCompanyId));
            }
        }

        if (dbUser == null) {
            throw new AuthenticationException("user didn't existed!");
        }

        if (!JWTUtil.verify(token, loginUser, dbUser.getPassword())) {
            throw new AuthenticationException("username or password error");
        }

        if(dbUser.getStatus() == 1){
            throw new AuthenticationException("user disabled");
        }
        return new SimpleAuthenticationInfo(dbUser, dbUser.getPassword(), getName());
    }
}
