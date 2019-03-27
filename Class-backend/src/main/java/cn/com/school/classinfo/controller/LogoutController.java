package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退出登录
 *
 * @author dongpp
 */
@Api(tags = "退出登录")
@Slf4j
@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    /**
     * 登出
     * @return 是否成功
     */
    @ApiOperation(value = "退出登录")
    @PostMapping
    public ResultMessage logout(){
        SecurityUtils.getSubject().logout();
        return ResultMessage.success();
    }

}
