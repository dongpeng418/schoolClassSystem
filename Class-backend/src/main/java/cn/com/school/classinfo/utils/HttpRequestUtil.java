/**
 *
 */
package cn.com.school.classinfo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author dongpp
 *
 */
@Slf4j
public class HttpRequestUtil {

    private static final String NGINX_IP_HEADER = "X-Real-IP";
    private static final String NGINX_URL_HEADER = "X-Real-Url";
    private static final String NGINX_X_FORWARDED_FOR = "C-Forwarded-For";
    private static final String API_URL = "api.open.1value.cn";


    /**
     * 功能描述: 获取ip（兼容nginx转发）
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ips = request.getHeader(NGINX_X_FORWARDED_FOR);
        String[] ipArray = StringUtils.split(ips, ",");
        if (ArrayUtils.isNotEmpty(ipArray)) {
            return StringUtils.trim(ipArray[0]);
        } else {
            String ip = request.getHeader(NGINX_IP_HEADER);
            if (StringUtils.isEmpty(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
    }

    /**
     * 从request中抽取当前url(兼容nginx转发模式)
     *
     * @param request
     * @see #NGINX_URL_HEADER
     * @return
     */
    public static String getRemoteUrl(HttpServletRequest request) {
        if (checkParamNull(request)) {
            return null;
        }
        String url = request.getHeader(NGINX_URL_HEADER);
        if (StringUtils.isEmpty(url)) {
            return request.getRequestURL().toString();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("NGINX_URL_HEADER:" + url);
            }
            return url;
        }
    }

    public static String getRealHost(HttpServletRequest request){
        String host;
        if(CorsUtils.isCorsRequest(request)){
            host = getHostByHeader(request, "Origin");
        }else{
            host = getHostName(request);
            if(API_URL.equals(host)){
                host = getHostByHeader(request, "Referer");
            }
        }
        return host;
    }

    private static String getHostByHeader(HttpServletRequest request, String header){
        String host = request.getHeader(header);
        URL url;
        try {
            url = new URL(host);
            host = url.getHost();
        } catch (MalformedURLException e) {
            log.error("get cors host error.", e);
        }
        return host;
    }
    /**
     *
     * 功能描述: <br>
     * 获取hostname
     *
     * @param request
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String getHostName(HttpServletRequest request) {
        return request.getHeader("Host");
    }

    /**
     * 获取用户代理
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent;
    }

    private static boolean checkParamNull(Object... params) {
        for (Object param : params) {
            if (null == param) {
                log.error("Invalid Parameter.");
                return true;
            }
        }
        return false;
    }
}
