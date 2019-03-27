/**
 *
 */
package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.CommonConstant;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.CustomerDomainQuery;
import cn.com.school.classinfo.common.query.EvalFieldQuery;
import cn.com.school.classinfo.common.query.UserQuery;
import cn.com.school.classinfo.model.CustomerCompany;
import cn.com.school.classinfo.model.CustomerDomain;
import cn.com.school.classinfo.model.SysEvaluationField;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.model.SysOrganization;
import cn.com.school.classinfo.model.SysResource;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.model.SysWebsiteConfig;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.service.CustomerCompanyService;
import cn.com.school.classinfo.service.CustomerDomainService;
import cn.com.school.classinfo.service.EvaluationFieldService;
import cn.com.school.classinfo.service.SysLogService;
import cn.com.school.classinfo.service.WebsiteConfigService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.utils.ObjectUtil;
import cn.com.school.classinfo.vo.SysRoleSimpleVO;
import cn.com.school.classinfo.vo.SysUserVO;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author dongpp
 * 系统权限管理_用户
 */
@Api(tags = "渠道-用户相关接口")
@Validated
@RestController
@RequestMapping("/api/tenant/auth/user")
public class TenantSysUserController {

    //渠道用户类型
    private final Integer TENANTUSERTYPE = 1;

    //客户用户类型
    private final Integer CUSTOMERUSERTYPE = 2;

    private final AuthCommonService authCommonService;

    private final PasswordService passwordService;

    private final CustomerCompanyService customerCompanyService;

    private final CustomerDomainService customerDomainService;

    private final EvaluationFieldService evaluationFieldService;

    private final SysLogService sysLogService;

    private final WebsiteConfigService websiteConfigService;

    @Autowired
    public TenantSysUserController(AuthCommonService authCommonService,
                    PasswordService passwordService,
                    CustomerCompanyService customerCompanyService,
                    CustomerDomainService customerDomainService,
                    EvaluationFieldService evaluationFieldService,
                    SysLogService sysLogService,
                    WebsiteConfigService websiteConfigService) {
        this.authCommonService = authCommonService;
        this.passwordService = passwordService;
        this.customerCompanyService = customerCompanyService;
        this.customerDomainService = customerDomainService;
        this.evaluationFieldService = evaluationFieldService;
        this.sysLogService = sysLogService;
        this.websiteConfigService = websiteConfigService;
    }

    /**
     * 添加用户接口
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "渠道用户_添加用户接口")
    @PostMapping("/t/add")
    public ResultMessage addUser(@Valid @RequestBody SysUser sysUser, HttpServletRequest request) {
        //渠道用户与客户用户差别在于，机构ID和客户公司ID不需要
        String loginUser = sysUser.getLoginUser();
        if (Strings.isNullOrEmpty(loginUser)) {
            return ResultMessage.requestError("用户信息【登录账户】 不能为空!");
        }
        String password = sysUser.getPassword();
        if (Strings.isNullOrEmpty(password)) {
            return ResultMessage.requestError("用户信息 【登录密码】 不能为空!");
        }

        if(!Strings.isNullOrEmpty(sysUser.getMobilePhone())) {
            if(sysUser.getMobilePhone().length()>=46) {
                return ResultMessage.requestError("用户信息 【手机号码】 字符过长!");
            }
        }

        if(!Strings.isNullOrEmpty(sysUser.getEmail())) {
            if(sysUser.getEmail().length()>=46) {
                return ResultMessage.requestError("用户信息 【邮箱地址】 字符过长!");
            }
        }

        //当前登录用户
        SysUser loginSysUser = CommonUtil.getLoginUser();
        SysUser existUser = authCommonService.findUserByLoginUserAndUTypeAndTId(loginUser,TENANTUSERTYPE,loginSysUser.getTenantId());
        if (existUser != null) {
            return ResultMessage.requestError("用户登录账户已存在：" + loginUser);
        }

        sysUser.setUserType(TENANTUSERTYPE);

        //加密并保存密码
        sysUser.setPassword(passwordService.encryptPassword(sysUser.getPassword()));

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.CREATE, accessIp);
        int result = authCommonService.insertSysUser(sysUser);
        if (result > 0) {
            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            sysLog.setOperateLog(String.format(SysLogConstant.TENANT_USER_ADD, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), sysUser.getLoginUser(), HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);

            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(sysUser, sysUserVO);
            return ResultMessage.success(sysUserVO);
        } else {
            return ResultMessage.requestError("用户信息添加失败!");
        }

    }


    /**
     * 添加用户接口
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "客户用户_添加用户接口")
    @PostMapping("/c/add")
    public ResultMessage addCustomerUser(@Valid @RequestBody SysUser sysUser, HttpServletRequest request) {
        //渠道用户与客户用户差别在于，机构ID和客户公司ID不需要
        String loginUser = sysUser.getLoginUser();
        if (Strings.isNullOrEmpty(loginUser)) {
            return ResultMessage.requestError("用户信息【登录账户】 不能为空!");
        }
        String password = sysUser.getPassword();
        if (Strings.isNullOrEmpty(password)) {
            return ResultMessage.requestError("用户信息 【登录密码】 不能为空!");
        }

        if(!Strings.isNullOrEmpty(sysUser.getMobilePhone())) {
            if(sysUser.getMobilePhone().length()>=46) {
                return ResultMessage.requestError("用户信息 【手机号码】 字符过长!");
            }
        }

        if(!Strings.isNullOrEmpty(sysUser.getEmail())) {
            if(sysUser.getEmail().length()>=46) {
                return ResultMessage.requestError("用户信息 【邮箱地址】 字符过长!");
            }
        }

        Integer customerCompanyId = sysUser.getCustomerCompanyId();
        if(customerCompanyId == null) {
            return ResultMessage.requestError("用户信息【客户公司ID】 值无效!");
        }
        if (customerCompanyId == 0) {
            return ResultMessage.requestError("用户信息【客户公司ID】 值无效!");
        }

        SysUser existUser = authCommonService.findUserByLoginUserAndCompanyId(loginUser,customerCompanyId);
        if (existUser != null) {
            return ResultMessage.requestError("用户登录账户已存在：" + loginUser);
        }

        List<SysUser> users = authCommonService.findUserByCIdAndUserType(CUSTOMERUSERTYPE,customerCompanyId);
        if(users.size()>0) {
            return ResultMessage.requestError("该客户公司下已存在管理员账户，不能添加多个");
        }

        String accessIp = HttpRequestUtil.getRemoteIp(request);

        CustomerCompany customerCompany = customerCompanyService.getById(customerCompanyId);
        SysOrganization sysOrganization = new SysOrganization();
        sysOrganization.setTenantId(sysUser.getTenantId());
        sysOrganization.setLevel(1);
        sysOrganization.setParentId(0);
        sysOrganization.setStatus(0);
        sysOrganization.setPriority(1);
        sysOrganization.setName(customerCompany.getName());
        CommonUtil.doCreateUpdateInfo(sysOrganization, CreateUpdateEnum.CREATE, accessIp);

        authCommonService.insertOrgInfo(sysOrganization);

        sysUser.setUserType(CUSTOMERUSERTYPE);
        sysUser.setOrganizationId(sysOrganization.getId());

        //加密并保存密码
        sysUser.setPassword(passwordService.encryptPassword(sysUser.getPassword()));
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.CREATE, accessIp);
        int result = authCommonService.insertSysUser(sysUser);

        //复制估价字段
        copyTenantFieldToCustomer(customerCompanyId,sysOrganization.getId());

        //复制网站配置
        //TODO
        copyWebsiteConfigToCustomer(customerCompanyId, sysOrganization.getId());

        if (result > 0) {
            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            sysLog.setOperateLog(String.format(SysLogConstant.CUSTOMER_ADD, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), sysUser.getLoginUser(), HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);

            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(sysUser, sysUserVO);
            return ResultMessage.success(sysUserVO);
        } else {
            return ResultMessage.requestError("用户信息添加失败!");
        }

    }

    /**
     * 修改用户接口
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "修改用户接口")
    @PostMapping("/t/edit")
    public ResultMessage editUser(@Valid @RequestBody SysUser sysUser, HttpServletRequest request) {

        Integer userId = sysUser.getId();
        if(userId == null) {
            return ResultMessage.requestError("用户信息 【id】 不能为空!");
        }

        if(!Strings.isNullOrEmpty(sysUser.getMobilePhone())) {
            if(sysUser.getMobilePhone().length()>=46) {
                return ResultMessage.requestError("用户信息 【手机号码】 字符过长!");
            }
        }

        if(!Strings.isNullOrEmpty(sysUser.getEmail())) {
            if(sysUser.getEmail().length()>=46) {
                return ResultMessage.requestError("用户信息 【邮箱地址】 字符过长!");
            }
        }

        SysUser existUser = authCommonService.findUserById(userId);

        if (existUser == null) {
            return ResultMessage.requestError("要更新的用户不存在：" + userId);
        }

        sysUser.setUserType(TENANTUSERTYPE);
        sysUser.setLoginUser(existUser.getLoginUser());

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.UPDATE, accessIp);
        if(StringUtils.isNotEmpty(sysUser.getPassword())||!sysUser.getPassword().equals("")){
            sysUser.setPassword(passwordService.encryptPassword(sysUser.getPassword()));
        }else {
            sysUser.setPassword(null);
        }
        int result = authCommonService.updateTenantSysUser(sysUser);
        if (result > 0) {

            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            String modifyDetail = ObjectUtil.getModifyInfo(sysUser, existUser);
            sysLog.setOperateLog(String.format(SysLogConstant.TENANT_USER_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), modifyDetail, HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);

            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(sysUser, sysUserVO);
            return ResultMessage.success(sysUserVO);
        } else {
            return ResultMessage.requestError("用户信息更新失败!");
        }
    }


    /**
     * 修改用户接口
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "修改用户接口")
    @PostMapping("/c/edit")
    public ResultMessage editCustomerUser(@Valid @RequestBody SysUser sysUser, HttpServletRequest request) {
        Integer userId = sysUser.getId();
        if(userId == null) {
            return ResultMessage.requestError("用户信息 【id】 不能为空!");
        }

        if(!Strings.isNullOrEmpty(sysUser.getMobilePhone())) {
            if(sysUser.getMobilePhone().length()>=46) {
                return ResultMessage.requestError("用户信息 【手机号码】 字符过长!");
            }
        }

        if(!Strings.isNullOrEmpty(sysUser.getEmail())) {
            if(sysUser.getEmail().length()>=46) {
                return ResultMessage.requestError("用户信息 【邮箱地址】 字符过长!");
            }
        }

        SysUser existUser = authCommonService.findUserById(userId);

        if (existUser == null) {
            return ResultMessage.requestError("要更新的用户不存在：" + userId);
        }

        sysUser.setUserType(TENANTUSERTYPE);
        sysUser.setLoginUser(existUser.getLoginUser());

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.UPDATE, accessIp);
        if(StringUtils.isNotEmpty(sysUser.getPassword())||!sysUser.getPassword().equals("")){
            sysUser.setPassword(passwordService.encryptPassword(sysUser.getPassword()));
        }else {//不做更新
            sysUser.setPassword(null);
        }
        sysUser.setCustomerCompanyId(sysUser.getCustomerCompanyId());
        int result = authCommonService.updateByLoginUserAndCId(sysUser);
        if (result > 0) {

            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            String modifyDetail = ObjectUtil.getModifyInfo(sysUser, existUser);
            sysLog.setOperateLog(String.format(SysLogConstant.CUSTOMER_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), modifyDetail, HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);

            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(sysUser, sysUserVO);
            return ResultMessage.success(sysUserVO);
        } else {
            return ResultMessage.requestError("用户信息更新失败!");
        }
    }


    /**
     * @return 失败与成功
     */
    @ApiOperation(value = "【渠道用户、渠道客户用户通用接口】启用、暂停、删除账户接口  0 启用  1停用")
    @PostMapping("/changeStatus")
    public ResultMessage changeStatus(@RequestParam(value = "userId") Integer userId,
                    @RequestParam(value = "status")
    @Range(min = 0, max = 1, message = "status 参数错误") Integer status,
    HttpServletRequest request) {

        SysUser existUser = authCommonService.findUserById(userId);
        if (existUser == null) {
            return ResultMessage.requestError("要更新的用户不存在：" + userId);
        }

        if(status < 0 || status > 1){
            return ResultMessage.requestError("用户状态错误：" + status);
        }

        SysUser sysUser = new SysUser();
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.UPDATE, accessIp);

        int result = authCommonService.updateSysUserStatusById(userId, status);
        if (result > 0) {

            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            String logStr;
            if(TENANTUSERTYPE.equals(existUser.getUserType())){
                if(status == 0){
                    logStr = SysLogConstant.TENANT_USER_ENABLE;
                }else{
                    logStr = SysLogConstant.TENANT_USER_DISABLE;
                }
            }else{
                if(status == 0){
                    logStr = SysLogConstant.CUSTOMER_ENABLE;
                }else{
                    logStr = SysLogConstant.CUSTOMER_DISABLE;
                }
            }
            sysLog.setOperateLog(String.format(logStr, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), existUser.getLoginUser(), HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);

            return ResultMessage.success();
        } else {
            return ResultMessage.requestError("用户信息更新失败!");
        }
    }

    /**
     * 分配角色接口
     *
     * @param userId         用户ID
     * @param roleIds        角色ID列表
     * @return 失败与成功
     * 分配角色
     */
    @ApiOperation(value = "分配角色接口")
    @PostMapping("/distributionRole")
    public ResultMessage distributionUserRole(@RequestParam Integer userId,
                    @RequestParam List<Integer> roleIds,
                    HttpServletRequest request) {
        if(Objects.isNull(userId)){
            return ResultMessage.paramError("userId");
        }
        if(CollectionUtils.isEmpty(roleIds)){
            return ResultMessage.paramError("roleIds");
        }
        boolean isSuccess = authCommonService.distributionUserRole(request, userId, roleIds);
        if (isSuccess) {
            SysUser existUser = authCommonService.findUserById(userId);
            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            String logStr;
            if(TENANTUSERTYPE.equals(existUser.getUserType())){
                logStr = SysLogConstant.TENANT_USER_DIST_ROLE;
            }else{
                logStr = SysLogConstant.CUSTOMER_DIST_ROLE;
            }
            StringBuilder rolIdStr = new StringBuilder();
            roleIds.forEach(roleId -> rolIdStr.append(roleId).append("、"));
            sysLog.setOperateLog(String.format(logStr, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), existUser.getLoginUser(), rolIdStr.substring(0, rolIdStr.length() - 1),
                            HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);
            return ResultMessage.success();
        } else {
            return ResultMessage.requestError("分配角色失败!");
        }
    }

    /**
     * 查看账户信息接口
     *
     * @return 失败与成功
     * 查看详情接口
     */
    @ApiOperation(value = "【渠道用户】查看账户信息接口")
    @GetMapping("/infoDetail")
    public ResultMessage infoDetail(@RequestParam(value = "userId") Integer userId) {
        SysUser detailUser = authCommonService.findUserById(userId);
        if (detailUser == null) {
            return ResultMessage.requestError("要查询的用户不存在：" + userId);
        } else {
            SysUserVO userVO = new SysUserVO();
            Integer organizationId = detailUser.getOrganizationId();
            if(organizationId!=null) {
                SysOrganization organization = authCommonService.findOrgInfoById(detailUser.getOrganizationId());
                if(Objects.nonNull(organization)) {
                    if(organization.getParentId()!=null) {
                        SysOrganization parentOrg = authCommonService.findOrgInfoById(organization.getParentId());
                        if(Objects.nonNull(parentOrg)){
                            if(!Strings.isNullOrEmpty(parentOrg.getName())) {
                                userVO.setParentOrgName(parentOrg.getName());
                            }
                        }
                    }
                    if(!Strings.isNullOrEmpty(organization.getName())) {
                        userVO.setOrganizationName(organization.getName());
                    }
                }
            }

            List<SysRoleSimpleVO> roleList = authCommonService.findRoleByUserId(userId);
            if(detailUser.getCustomerCompanyId()!=null) {
                CustomerCompany customerCompany = customerCompanyService.getById(detailUser.getCustomerCompanyId());
                if(customerCompany!=null) {
                    if(!Strings.isNullOrEmpty(customerCompany.getName())) {
                        userVO.setCustomerCompanyName(customerCompany.getName());
                    }
                }
            }
            BeanUtils.copyProperties(detailUser, userVO);
            userVO.setRoleList(roleList);
            return ResultMessage.success(userVO);
        }
    }


    /**
     * 更新账户密码信息接口
     *
     * @param oldPwd 老密码
     * @param newPwd 新密码
     * @return 失败与成功
     * 查看详情接口
     */
    @ApiOperation(value = "更新渠道用户_账户密码信息接口")
    @PostMapping("/t/updatePwd")
    public ResultMessage updateUserLoginPasswd(@RequestParam(value = "oldPwd") String oldPwd,
                    @RequestParam(value = "newPwd") String newPwd,
                    HttpServletRequest request) {

        if(Strings.isNullOrEmpty(oldPwd)) {
            return ResultMessage.requestError("原始密码不能为空");
        }
        if(Strings.isNullOrEmpty(newPwd)) {
            return ResultMessage.requestError("新密码不能为空");
        }

        SysUser currentUser = CommonUtil.getLoginUser();
        SysUser detailUser = authCommonService.findUserById(currentUser.getId());
        if (detailUser == null) {
            return ResultMessage.requestError("要查询的用户不存在");
        }

        if (!passwordService.passwordsMatch(oldPwd, detailUser.getPassword())) {
            return ResultMessage.requestError("原密码错误");
        }
        SysUser newUser = new SysUser();
        newUser.setLoginUser(detailUser.getLoginUser());
        newUser.setPassword(passwordService.encryptPassword(newPwd));
        newUser.setUserType(TENANTUSERTYPE);
        newUser.setCustomerCompanyId(detailUser.getCustomerCompanyId());
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(newUser, CreateUpdateEnum.UPDATE, accessIp);
        authCommonService.updateTenantSysUser(newUser);
        return ResultMessage.success();
    }


    /**
     * 更新账户密码信息接口
     *
     * @param oldPwd 老密码
     * @param newPwd 新密码
     * @return 失败与成功
     * 查看详情接口
     */
    @ApiOperation(value = "更新渠道账户密码_信息接口")
    @PostMapping("/c/updatePwd")
    public ResultMessage updateCustomerUserLoginPasswd(@RequestParam(value = "oldPwd") String oldPwd,
                    @RequestParam(value = "newPwd") String newPwd,
                    HttpServletRequest request) {

        if(Strings.isNullOrEmpty(oldPwd)) {
            return ResultMessage.requestError("原始密码不能为空");
        }
        if(Strings.isNullOrEmpty(newPwd)) {
            return ResultMessage.requestError("新密码不能为空");
        }

        SysUser currentUser = CommonUtil.getLoginUser();
        SysUser detailUser = authCommonService.findUserById(currentUser.getId());
        if (detailUser == null) {
            return ResultMessage.requestError("要查询的用户不存在");
        }

        if (!passwordService.passwordsMatch(oldPwd, detailUser.getPassword())) {
            return ResultMessage.requestError("原密码错误");
        }
        SysUser newUser = new SysUser();
        newUser.setLoginUser(detailUser.getLoginUser());
        newUser.setPassword(passwordService.encryptPassword(newPwd));
        newUser.setUserType(CUSTOMERUSERTYPE);
        newUser.setCustomerCompanyId(detailUser.getCustomerCompanyId());
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(newUser, CreateUpdateEnum.UPDATE, accessIp);
        authCommonService.updateByLoginUserAndCId(newUser);
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog, detailUser);
        sysLog.setOperateLog(String.format(SysLogConstant.USER_MODIFY_PWD, detailUser.getLoginUser(),
                        DateUtil.nowToString(), HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @ApiOperation(value = "获取渠道用户列表，organizationIds不用传")
    @GetMapping("/t/list")
    public ResultMessage list(@ModelAttribute UserQuery query) {
        SysUser currentUser = CommonUtil.getLoginUser();
        int tenantId = currentUser.getTenantId();
        int userType = TENANTUSERTYPE;
        query.setTenantId(tenantId);
        query.setUserType(userType);
        PageInfo<SysUserVO> pageInfo = authCommonService.findTenantUserList(query);
        return ResultMessage.success(pageInfo);
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @ApiOperation(value = "获取渠道客户用户列表，organizationIds不用传")
    @GetMapping("/c/list")
    public ResultMessage customerUserLists(@ModelAttribute UserQuery query) {
        SysUser currentUser = CommonUtil.getLoginUser();
        int tenantId = currentUser.getTenantId();
        int userType = CUSTOMERUSERTYPE;
        query.setTenantId(tenantId);
        query.setUserType(userType);
        PageInfo<SysUserVO> pageInfo = authCommonService.findCustomerUserList(query);
        return ResultMessage.success(pageInfo);
    }


    /**
     * 获取登录用户信息
     *
     * @return 登录用户信息
     */
    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/login-user")
    public ResultMessage loginUser() {
        SysUser currentUser = CommonUtil.getLoginUser();
        SysUserVO userVO = new SysUserVO();
        if(currentUser.getOrganizationId() != null) {
            SysOrganization organization = authCommonService.findOrgInfoById(currentUser.getOrganizationId());
            if(organization != null) {
                userVO.setOrganizationName(organization.getName());
                if(organization.getParentId() !=null) {
                    SysOrganization parentOrg = authCommonService.findOrgInfoById(organization.getParentId());
                    if(Objects.nonNull(parentOrg)){
                        userVO.setParentOrgName(parentOrg.getName());
                    }
                }
            }
        }
        BeanUtils.copyProperties(currentUser, userVO);
        return ResultMessage.success(userVO);
    }

    /**
     * 修改登录用户信息
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "修改渠道用户信息接口")
    @PostMapping("/t/login-user/edit")
    public ResultMessage editLoginUser(@RequestParam String loginUser,
                    @RequestParam String email,
                    @RequestParam String phone,
                    HttpServletRequest request) {
        if (Objects.isNull(loginUser)) {
            return ResultMessage.paramError("loginUser");
        }

        SysUser currentUser = CommonUtil.getLoginUser();
        if(!currentUser.getLoginUser().equals(loginUser)) {
            return ResultMessage.requestError("要更新的用户与当前登录用户信息不一致：" + loginUser);
        }

        if (StringUtils.isBlank(email)) {
            return ResultMessage.paramError("email");
        }

        if (StringUtils.isBlank(phone)) {
            return ResultMessage.paramError("phone");
        }

        if(phone.length()>=46) {
            return ResultMessage.requestError("用户信息 【手机号码】 字符过长!");
        }

        if(email.length()>=46) {
            return ResultMessage.requestError("用户信息 【邮箱地址】 字符过长!");
        }

        SysUser existUser = authCommonService.findUserByLoginUserAndUTypeAndTId(loginUser,TENANTUSERTYPE,currentUser.getTenantId());
        if (existUser == null) {
            return ResultMessage.requestError("要更新的用户不存在：" + loginUser);
        }
        SysUser sysUser = new SysUser();
        sysUser.setLoginUser(loginUser);
        sysUser.setEmail(email);
        sysUser.setMobilePhone(phone);
        sysUser.setUserType(TENANTUSERTYPE);
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.UPDATE, accessIp);

        int result = authCommonService.updateTenantSysUser(sysUser);
        if (result > 0) {
            return ResultMessage.success();
        } else {
            return ResultMessage.requestError("用户信息更新失败!");
        }
    }

    /**
     * 修改登录用户信息
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "修改渠道客户用户信息接口")
    @PostMapping("/c/login-user/edit")
    public ResultMessage editCustomerLoginUser(@RequestParam String loginUser,
                    @RequestParam String email,
                    @RequestParam String phone,
                    HttpServletRequest request) {
        if (Objects.isNull(loginUser)) {
            return ResultMessage.paramError("loginUser");
        }

        SysUser currentUser = CommonUtil.getLoginUser();
        if(!currentUser.getLoginUser().equals(loginUser)) {
            return ResultMessage.requestError("要更新的用户与当前登录用户信息不一致：" + loginUser);
        }

        if (StringUtils.isBlank(email)) {
            return ResultMessage.paramError("email");
        }

        if (StringUtils.isBlank(phone)) {
            return ResultMessage.paramError("phone");
        }

        if(phone.length()>=46) {
            return ResultMessage.requestError("用户信息 【手机号码】 字符过长!");
        }

        if(email.length()>=46) {
            return ResultMessage.requestError("用户信息 【邮箱地址】 字符过长!");
        }

        SysUser existUser = authCommonService.findUserByLoginUserAndCompanyId(loginUser,currentUser.getCustomerCompanyId());
        if (existUser == null) {
            return ResultMessage.requestError("要更新的用户不存在：" + loginUser);
        }
        SysUser sysUser = new SysUser();
        sysUser.setLoginUser(loginUser);
        sysUser.setEmail(email);
        sysUser.setMobilePhone(phone);
        sysUser.setUserType(CUSTOMERUSERTYPE);
        sysUser.setCustomerCompanyId(existUser.getCustomerCompanyId());
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.UPDATE, accessIp);

        int result = authCommonService.updateByLoginUserAndCId(sysUser);
        if (result > 0) {
            return ResultMessage.success();
        } else {
            return ResultMessage.requestError("用户信息更新失败!");
        }
    }

    /**
     * 修改登录用户信息
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "获取渠道登录用户的角色权限信息")
    @GetMapping("/t/login-user/perms")
    public ResultMessage loginUserRoleList() {
        SysUser sysUser = CommonUtil.getLoginUser();
        List<SysResource> resources = authCommonService.selectUserPermissionsByUserType(sysUser.getLoginUser(),TENANTUSERTYPE);
        return ResultMessage.success(resources);
    }

    /**
     * 修改登录用户信息
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "获取渠道客户登录用户的角色权限信息")
    @GetMapping("/c/login-user/perms")
    public ResultMessage loginCustomerUserRoleList() {
        SysUser sysUser = CommonUtil.getLoginUser();
        List<SysResource> resources = authCommonService.selectUserPermissionsByUserType(sysUser.getLoginUser(),CUSTOMERUSERTYPE);
        return ResultMessage.success(resources);
    }

    /**
     * 新建客户用户时将渠道设置的估价字段列表复制一份到客户下
     *
     * @param customerCompanyId 客户公司ID
     */
    private void copyTenantFieldToCustomer(Integer customerCompanyId,Integer orgId){
        EvalFieldQuery query = new EvalFieldQuery();
        query.setType(CommonConstant.TENANT_TYPE);
        query.setTenantId(CommonUtil.getTenantId());
        List<SysEvaluationField> fieldList = evaluationFieldService.list(query);
        if(CollectionUtils.isNotEmpty(fieldList)){
            fieldList.forEach(sysEvaluationField -> {
                sysEvaluationField.setId(null);
                sysEvaluationField.setType(CommonConstant.CUSTOMER_TYPE);
                CommonUtil.doEvalCreateUpdateInfo(sysEvaluationField, CreateUpdateEnum.CREATE);
                sysEvaluationField.setCompanyId(customerCompanyId);
                sysEvaluationField.setOrganizationId(orgId);
            });
        }
        evaluationFieldService.insert(fieldList);
    }

    /**
     * 复制网站配置到客户
     * @param orgId 机构ID
     */
    private void copyWebsiteConfigToCustomer(Integer customerCompanyId, Integer orgId){
        Integer tenantId = CommonUtil.getTenantId();
        SysWebsiteConfig config = websiteConfigService.getByTenantId(CommonUtil.getTenantId(), CommonConstant.TENANT_TYPE);
        if(Objects.nonNull(config)){
            config.setId(null);
            config.setType(CommonConstant.CUSTOMER_TYPE);
            CommonUtil.doEvalCreateUpdateInfo(config, CreateUpdateEnum.CREATE);
            config.setOrganizationId(orgId);
            CustomerDomain domain = getCustomerDomain(customerCompanyId, tenantId);
            if(Objects.nonNull(domain)){
                config.setDomain(domain.getDomain());
            }
            websiteConfigService.add(config);
        }
    }

    /**
     * 获取客户域名
     * @param customerCompanyId 客户公司ID
     * @param tenantId 渠道ID
     * @return
     */
    private CustomerDomain getCustomerDomain(Integer customerCompanyId, Integer tenantId){
        CustomerDomainQuery query = new CustomerDomainQuery();
        query.setCompanyId(customerCompanyId);
        query.setTenantId(tenantId);
        return customerDomainService.getByQuery(query);
    }

}
