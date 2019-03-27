package cn.com.school.classinfo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis配置
 * @author dongpp
 * @date 2018/10/23
 */
@Configuration
@MapperScan(basePackages = "cn.com.school.classinfo.mapper")
public class MybatisConfig {

}
