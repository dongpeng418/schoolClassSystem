package cn.com.school.classinfo.authorization;

import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.utils.JWTUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private AuthCommonService authCommonService;

    public JWTFilter(AuthCommonService authCommonService) {
        this.authCommonService = authCommonService;
    }

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String authorization = getToken(request, response);
        return StringUtils.isNotBlank(authorization);
    }

    /**
     * 执行登录
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        String authorization = getToken(request, response);
        JWTToken token = new JWTToken(authorization);
        try {
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            getSubject(request, response).login(token);
        } catch (Exception e) {
            log.error("failed to login :", e);
            return false;
        }
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 获取token
     *
     * @param request 请求
     * @return token
     */
    private String getToken(ServletRequest request, ServletResponse response) {
        String authorization = WebUtils.toHttp(response).getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.isNotBlank(authorization)){
            return authorization;
        }
        authorization = getAuthzHeader(request);
        //如果header中没有token，则从参数中获取（下载文件没有办法传递token）
        if (StringUtils.isBlank(authorization)) {
            authorization = request.getParameter(AUTHORIZATION_HEADER);
        }
        return authorization;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean allowed = false;
        if (request instanceof HttpServletRequest) {
            if ("OPTIONS".equals(((HttpServletRequest) request).getMethod().toUpperCase())) {
                return true;
            }
        }

        //判断token是否过期，如果过期并且过期时间没有超过默认的30分钟，则重新生成一个token并回传前端
        String authorization = getToken(request, response);
        if (StringUtils.isNotBlank(authorization) && JWTUtil.isExpire(authorization)) {
            String loginUser = JWTUtil.getUsername(authorization);
            String userType = JWTUtil.getUserType(authorization);
            String customerCompanyId = JWTUtil.getCustomerCompanyId(authorization);
            SysUser dbUser = null;
            if(!Strings.isNullOrEmpty(userType)) {
                //渠道用户
                if("1".equals(userType)) {
                    String tenantId = JWTUtil.getTenantId(authorization);
                    if(StringUtils.isBlank(tenantId)){
                        return false;
                    }
                    dbUser = authCommonService.findUserByLoginUserAndUTypeAndTId(loginUser,1, Integer.parseInt(tenantId));
                }else {//客户用户或者渠道客户用户
                    if(Strings.isNullOrEmpty(customerCompanyId)) {
                        return false;
                    }
                    dbUser = authCommonService.findUserByLoginUserAndCompanyId(loginUser,Integer.parseInt(customerCompanyId));
                }
            }
            if(Objects.isNull(dbUser)){
                return false;
            }
            authorization = JWTUtil.regenerateToken(authorization, dbUser.getPassword());
            if (StringUtils.isNotBlank(authorization)) {
                refreshToken(response, authorization);
            }
        }

        if (isLoginAttempt(request, response)) {
            allowed = executeLogin(request, response);
        }
        if (!allowed) {
            response401(request, response);
        }
        return allowed;
    }



    /**
     * 将重新生成的token放入响应中，前端来更新这个token
     *
     * @param response 响应
     * @param token    新token
     */
    private void refreshToken(ServletResponse response, String token) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.addHeader(AUTHORIZATION_HEADER, token);
    }

    /**
     * 将非法请求跳转到 /401
     */
    private void response401(ServletRequest req, ServletResponse resp) {
        try {
            req.getRequestDispatcher("/api/auth/error/noLogin").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
