/**
 *
 */
package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.vo.SysUserVO;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dongpp
 * 系统权限管理_渠道操作相关
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/api/auth/tenant")
public class SysTenantController {

    private final AuthCommonService authCommonService;

    private final PasswordService passwordService;

    @Autowired
    public SysTenantController(AuthCommonService authCommonService,
                    PasswordService passwordService) {
        this.authCommonService = authCommonService;
        this.passwordService = passwordService;
    }

    /**
     * 修改渠道用户接口
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "修改渠道用户信息接口")
    @PostMapping("/edit")
    public ResultMessage editUser(@RequestBody SysUser sysUser, HttpServletRequest request) {
        String loginUser = sysUser.getLoginUser();
        if (Strings.isNullOrEmpty(loginUser)) {
            return ResultMessage.requestError("用户信息 【登录账户】 不能为空!");
        }

        if(sysUser.getStatus()< 0 || sysUser.getStatus() > 1){
            return ResultMessage.requestError("用户状态错误：" + sysUser.getStatus());
        }

        if(StringUtils.isEmpty(sysUser.getPassword())){
            return ResultMessage.paramError("password");
        }

        SysUser existUser = authCommonService.findUserByLoginUser(loginUser);
        if (existUser == null) {
            return ResultMessage.requestError("要更新的用户不存在：" + loginUser);
        }

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.UPDATE, accessIp);
        //产品原型示例，渠道个人中心只能更新【密码】、【邮箱】、【手机】
        existUser.setPassword(passwordService.encryptPassword(sysUser.getPassword()));
        existUser.setEmail(sysUser.getEmail());
        existUser.setMobilePhone(sysUser.getMobilePhone());
        int result = authCommonService.updateTenantSysUser(existUser);
        if (result > 0) {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(existUser, sysUserVO);
            return ResultMessage.success(sysUserVO);
        } else {
            return ResultMessage.requestError("用户信息更新失败!");
        }
    }

}
