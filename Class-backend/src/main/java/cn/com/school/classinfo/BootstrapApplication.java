package cn.com.school.classinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 * @author dongpp
 * @date 2018-10-22
 */
@SpringBootApplication
public class BootstrapApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootstrapApplication.class);
        application.run(args);
    }
}