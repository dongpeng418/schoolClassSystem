/**
 *
 */
package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.model.SysTenant;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.service.SysLogService;
import cn.com.school.classinfo.utils.CaptchaUtil;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.utils.JWTUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author dongpp
 *
 */
@Api(tags = "渠道-登录相关")
@Slf4j
@RestController
@RequestMapping("/api/tenant")
public class TenantLoginController {

    private static final String CAPTCHA = "captcha";
    private static final String CAPTCHAIDENTITY = "identity";

    private final PasswordService passwordService;

    private final AuthCommonService authCommonService;

    private final SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public TenantLoginController(PasswordService passwordService,
                    AuthCommonService authCommonService,
                    SysLogService sysLogService) {
        this.passwordService = passwordService;
        this.authCommonService = authCommonService;
        this.sysLogService = sysLogService;
    }

    /**
     * 登录入口
     * @return 是否成功
     */
    @ApiOperation(value = "登录入口")
    @PostMapping("/login")
    public ResultMessage login(@NotBlank(message = "userName 不能为空") @RequestParam String userName,
                    @NotBlank(message = "password 不能为空") @RequestParam String password,
                    @NotBlank(message = "captcha 不能为空") @RequestParam String captcha,
                    @NotBlank(message = "identity 不能为空") @RequestParam String identity,
                    @RequestParam(required = false, defaultValue = "false") Boolean rememberMe,
                    HttpServletRequest request){


        Session session = SecurityUtils.getSubject().getSession();
        Object sessionAttr = session.getAttribute(CAPTCHA);
        if(Objects.isNull(sessionAttr)){
            return ResultMessage.authError("请获取验证码");
        }
        //验证码只使用一次
        session.removeAttribute(CAPTCHA);
        String host = HttpRequestUtil.getRealHost(request);

        SysTenant sysTenant = new SysTenant();
        sysTenant.setDomain(host);
        SysTenant dbTenant = authCommonService.selectByDomain(sysTenant);
        if(dbTenant == null) {
            return ResultMessage.authError("访问域名渠道尚未激活");
        }
        SysUser dbUser = authCommonService.findUserByLoginUserAndUTypeAndTId(userName,1, dbTenant.getId());
        if(dbUser == null) {
            return ResultMessage.authError("渠道用户不存在:"+userName);
        }
        if(dbUser.getUserType() !=1) {
            return ResultMessage.authError("渠道用户不存在:"+userName);
        }
        //密码是否正确
        if(!passwordService.passwordsMatch(password, dbUser.getPassword())) {
            return ResultMessage.authError("用户名或密码不正确");
        }

        String sessionIdentity = (String) session.getAttribute(CAPTCHAIDENTITY);
        session.removeAttribute(CAPTCHAIDENTITY);

        if(!sessionIdentity.equals(identity)) {
            return ResultMessage.authError("验证码不对");
        }

        //判断验证码是否正常
        String sessionCaptcha = (String) sessionAttr;
        if(!sessionCaptcha.equalsIgnoreCase(captcha)){
            return ResultMessage.authError("验证码错误");
        }

        if(dbUser.getStatus() == 1) {
            return ResultMessage.authError("账户已停用");
        }

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        SysUser sysUser = new SysUser();
        sysUser.setLoginUser(dbUser.getLoginUser());
        if(!Strings.isNullOrEmpty(dbUser.getCurrentLoginIp())) {
            sysUser.setLastLoginIp(dbUser.getCurrentLoginIp());
        }
        if(dbUser.getCurrentLoginTime()!=null) {
            sysUser.setLastLoginTime(dbUser.getCurrentLoginTime());
        }
        sysUser.setCurrentLoginIp(accessIp);
        sysUser.setCurrentLoginTime(DateUtil.now());
        sysUser.setUserType(1);//渠道用户
        authCommonService.updateTenantSysUser(sysUser);
        String token;
        if(Objects.isNull(rememberMe) || !rememberMe){
            token = JWTUtil.normalSignByUserTypeAndTenantId(userName, dbUser.getPassword(),1,1,dbTenant.getId());
        }else{
            token = JWTUtil.rememberMeSignByUserTypeAndTenantId(userName, dbUser.getPassword(),1,1,dbTenant.getId());
        }
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog, dbUser);
        sysLog.setOperateLog(String.format(SysLogConstant.USER_LOGIN, dbUser.getLoginUser(),
                        DateUtil.nowToString(), HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success(token);
    }

    /**
     * 登出
     * @return 是否成功
     */
    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public ResultMessage logout(){
        SecurityUtils.getSubject().logout();
        return ResultMessage.success();
    }

    /**
     * 获取验证码
     * @return 验证码
     */
    @ApiOperation(value = "获取验证码")
    @GetMapping("/captcha")
    public ResultMessage captcha() throws IOException {
        //生成验证码并将其转成base64
        CaptchaUtil captchaUtil = new CaptchaUtil();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        captchaUtil.write(outputStream);
        byte[] bytes = outputStream.toByteArray();
        String base64 = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        base64 = "data:image/jpg;base64," + base64;
        SecurityUtils.getSubject().getSession().setAttribute(CAPTCHA, captchaUtil.getCode());

        String identity = UUID.randomUUID().toString().replace("-", "");

        SecurityUtils.getSubject().getSession().setAttribute(CAPTCHAIDENTITY, identity);

        Map<String,String> result = Maps.newHashMap();
        result.put("captchaCode", captchaUtil.getCode());
        result.put("identity", identity);
        result.put("captcha", base64);
        return ResultMessage.success().data(result);
    }

}
