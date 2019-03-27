/**
 *
 */
package cn.com.school.classinfo.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author dongpp
 *
 */
@Configuration
//maxInactiveIntervalInSeconds session超时时间,单位秒
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 6000)
public class RedisSessionConfig {
}
