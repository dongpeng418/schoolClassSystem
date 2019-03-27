package cn.com.school.classinfo.authorization;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 定义权限认证过滤器，主要用于权限认证失败的跳转功能
 *
 * @author dongpp
 * @date 2018/12/11
 */
@Slf4j
public class AvsPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        String unAuthUrl = "/api/auth/error/401";
        try {
            request.getRequestDispatcher(unAuthUrl).forward(request, response);
        } catch (ServletException e) {
            log.error("dispatcher error, url: {}, msg: {}", unAuthUrl, e);
        }
        return false;
    }
}
