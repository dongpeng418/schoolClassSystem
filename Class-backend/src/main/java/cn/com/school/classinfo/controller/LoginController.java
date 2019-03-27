/**
 *
 */
package cn.com.school.classinfo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

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

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.model.CustomerDomain;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.service.CustomerDomainService;
import cn.com.school.classinfo.utils.CaptchaUtil;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongpp
 *
 */
@Api(tags = "登录相关")
@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private static final String CAPTCHA = "captcha";
    private static final String CAPTCHAIDENTITY = "identity";

    private final PasswordService passwordService;

    private final AuthCommonService authCommonService;
    private final CustomerDomainService customerDomainService;

    @Autowired
    public LoginController(PasswordService passwordService,
                    AuthCommonService authCommonService,
                    CustomerDomainService customerDomainService) {
        this.passwordService = passwordService;
        this.authCommonService = authCommonService;
        this.customerDomainService = customerDomainService;
    }

    /**
     * 登录入口
     * @return 是否成功
     */
    @ApiOperation(value = "登录入口")
    @PostMapping
    public ResultMessage login(@NotBlank(message = "userName 不能为空") @RequestParam String userName,
                    @NotBlank(message = "password 不能为空") @RequestParam String password,
                    @NotBlank(message = "captcha 不能为空") @RequestParam String captcha,
                    @NotBlank(message = "identity 不能为空") @RequestParam String identity,
                    @RequestParam(required = false, defaultValue = "false") Boolean rememberMe,
                    HttpServletRequest request){
        String host = HttpRequestUtil.getRealHost(request);
        CustomerDomain  customerDomain = customerDomainService.getByServerIp(host);
        if(customerDomain == null) {
            return ResultMessage.authError("客户服务尚未激活");
        }

        int customerCompanyId = customerDomain.getCompanyId();

        //用户是否存在
        SysUser dbUser = authCommonService.findUserByLoginUserAndCompanyId(userName,customerCompanyId);
        if(dbUser == null) {
            return ResultMessage.authError("该客户公司不存在该账户:"+userName);
        }
        //密码是否正确
        if(!passwordService.passwordsMatch(password, dbUser.getPassword())) {
            return ResultMessage.authError("用户名或密码不正确");
        }

        //TODO 压力测试增加通用验证码
        if(!"TEST".equals(captcha)){
            Session session = SecurityUtils.getSubject().getSession();
            Object sessionAttr = session.getAttribute(CAPTCHA);
            if(Objects.isNull(captcha)){
                return ResultMessage.authError("请获取验证码");
            }
            //验证码只使用一次
            session.removeAttribute(CAPTCHA);
            //判断验证码是否正常
            String sessionCaptcha = (String) sessionAttr;
            /* if(!sessionCaptcha.equalsIgnoreCase(captcha)){
                return ResultMessage.authError("验证码错误");
            }
            String sessionIdentity = (String) session.getAttribute(CAPTCHAIDENTITY);
            session.removeAttribute(CAPTCHAIDENTITY);
            if(!sessionIdentity.equals(identity)) {
                return ResultMessage.authError("验证码错误");
            }*/
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
        sysUser.setCustomerCompanyId(dbUser.getCustomerCompanyId());
        authCommonService.updateByLoginUserAndCId(sysUser);
        String token;
        if(Objects.isNull(rememberMe) || !rememberMe){
            token = JWTUtil.normalSignByUserType(userName, dbUser.getPassword(),2,dbUser.getCustomerCompanyId());
        }else{
            token = JWTUtil.rememberMeSignByUserType(userName, dbUser.getPassword(),2,dbUser.getCustomerCompanyId());
        }
        return ResultMessage.success(token);
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

        String identity = UUID.randomUUID().toString().replace("-", "");

        SecurityUtils.getSubject().getSession().setAttribute(CAPTCHA, captchaUtil.getCode());
        SecurityUtils.getSubject().getSession().setAttribute(CAPTCHAIDENTITY, identity);

        Map<String,String> result = Maps.newHashMap();
        result.put("identity", identity);
        result.put("captcha", base64);
        return ResultMessage.success().data(result);
    }

}
