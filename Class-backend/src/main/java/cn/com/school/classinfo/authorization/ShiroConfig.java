package cn.com.school.classinfo.authorization;

import cn.com.school.classinfo.model.SysResource;
import cn.com.school.classinfo.service.AuthCommonService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class ShiroConfig {


    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(CityReRealm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        realm.setCredentialsMatcher(new JWTCredentialsMatcher());
        realm.setCachingEnabled(false);

        // 使用自己的realm
        manager.setRealm(realm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager, AuthCommonService authCommonService) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = Maps.newLinkedHashMap();
        filterMap.put("jwt", new JWTFilter(authCommonService));
        filterMap.put("avsPerms", new AvsPermissionsAuthorizationFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = Maps.newLinkedHashMapWithExpectedSize(64);
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/api/login/**", "anon");
        filterRuleMap.put("/api/logout", "logout");
        filterRuleMap.put("/api/tenant/login", "anon");
        filterRuleMap.put("/api/tenant/captcha", "anon");
        filterRuleMap.put("/api/tenant/logout", "anon");
        filterRuleMap.put("/api/auth/error/**", "anon");
        filterRuleMap.put("/api/site-config/get", "anon");
        filterRuleMap.put("/api/site-config/images", "anon");
        filterRuleMap.put("/api/dict/**", "anon");
        filterRuleMap.put("/api/region/**", "anon");
        filterRuleMap.put("/api/tenant/report-config/get", "anon");
        filterRuleMap.put("/api/tenant/report-config/images", "anon");
        filterRuleMap.put("/api/tenant/site-config/get", "anon");
        filterRuleMap.put("/api/tenant/site-config/images", "anon");
        filterRuleMap.put("/api/tenant/customer-company/images", "anon");
        List<SysResource> resources = authCommonService.selectNeedAuthcs();
        String permissionStr;
        for (SysResource sysResource : resources) {
            permissionStr = "jwt, avsPerms[\""+sysResource.getCode()+"\"]";
            filterRuleMap.put(sysResource.getUrl(), permissionStr);
        }
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/api/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * 可以扩展凭证匹配器，实现 输入密码错误次数后锁定等功能，下一次
     * @return
     */
    @Bean(name="credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("SHA-256");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
        //storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 注意坑点 : 密码校验 , 这里因为是JWT形式,就无需密码校验和加密,直接让其返回为true(如果不设置的话,该值默认为false,即始终验证不通过)
     */
    private CredentialsMatcher credentialsMatcher() {
        return (token, info) -> true;
    }

    /**
     * 密码加密码，检验服务
     *
     * @return DefaultPasswordService
     */
    @Bean
    public PasswordService passwordService(){
        return new DefaultPasswordService();
    }

}
