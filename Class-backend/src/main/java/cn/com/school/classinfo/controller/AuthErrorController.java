package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.ResultMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 权限认证失败信息
 *
 * @author dongpp
 * @date 2018/12/6
 */
@ApiIgnore
@RestController
@RequestMapping("/api/auth/error/")
public class AuthErrorController {
    /**
     * 没有权限
     *
     * @return 失败信息
     */
    @GetMapping("/401")
    public ResultMessage authFiledGet() {
        return ResultMessage.authError("no access permission");
    }

    /**
     * 没有权限
     *
     * @return 失败信息
     */
    @PostMapping("/401")
    public ResultMessage authFiledPost() {
        return ResultMessage.authError("no access permission");
    }

    /**
     * 没有登录
     *
     * @return 失败信息
     */
    @GetMapping("/noLogin")
    public ResultMessage loginFiledGet() {
        return ResultMessage.authError("请登录");
    }

    /**
     * 没有登录
     *
     * @return 失败信息
     */
    @PostMapping("/noLogin")
    public ResultMessage loginFiledPost() {
        return ResultMessage.authError("请登录");
    }
}
