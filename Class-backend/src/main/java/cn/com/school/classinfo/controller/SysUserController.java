/**
 *
 */
package cn.com.school.classinfo.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.UserQuery;
import cn.com.school.classinfo.model.SysOrganization;
import cn.com.school.classinfo.model.SysResource;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.service.StatisticsService;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.vo.SysRoleSimpleVO;
import cn.com.school.classinfo.vo.SysUserVO;
import cn.com.school.classinfo.vo.UserStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dongpp
 * 系统权限管理_用户
 */
@Api(tags = "用户相关接口")
@Validated
@RestController
@RequestMapping("/api/auth/user")
public class SysUserController {

    //渠道用户类型
    private final Integer USERTYPE = 3;

    private final AuthCommonService authCommonService;

    private final PasswordService passwordService;

    private final StatisticsService statisticsService;

    @Autowired
    public SysUserController(AuthCommonService authCommonService,
                    PasswordService passwordService,
                    StatisticsService statisticsService) {
        this.authCommonService = authCommonService;
        this.passwordService = passwordService;
        this.statisticsService = statisticsService;
    }

    /**
     * 添加用户接口
     *
     * @return 失败与成功
     */
    @ApiOperation(value = "添加用户接口")
    @PostMapping("/add")
    public ResultMessage addUser(@Valid @RequestBody SysUser sysUser, HttpServletRequest request) {

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

        Integer customerCompanyId = CommonUtil.getLoginUser().getCustomerCompanyId();//登录信息中获取
        if(customerCompanyId == null) {
            return ResultMessage.requestError("用户信息【客户公司ID】 值无效!");
        }
        if (customerCompanyId.intValue() == 0) {
            return ResultMessage.requestError("用户信息【客户公司ID】 值无效!");
        }

        Integer organizationId = sysUser.getOrganizationId();//登录信息中获取
        if(organizationId == null) {
            return ResultMessage.requestError("用户信息【客户公司ID】 值无效!");
        }
        if (organizationId.intValue() == 0) {
            return ResultMessage.requestError("用户信息【公司ID】 值无效!");
        }

        SysUser existUser = authCommonService.findUserByLoginUserAndCompanyId(loginUser,customerCompanyId);
        if (existUser != null) {
            return ResultMessage.requestError("用户登录账户已存在：" + loginUser);
        }

        //加密并保存密码
        sysUser.setPassword(passwordService.encryptPassword(sysUser.getPassword()));

        sysUser.setUserType(USERTYPE);
        sysUser.setCustomerCompanyId(customerCompanyId);

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.CREATE, accessIp);
        int result = authCommonService.insertSysUser(sysUser);
        if (result > 0) {
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
    @PostMapping("/edit")
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

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysUser, CreateUpdateEnum.UPDATE, accessIp);
        if(StringUtils.isNotEmpty(sysUser.getPassword())||!sysUser.getPassword().equals("")){//可以不修改密码
            sysUser.setPassword(passwordService.encryptPassword(sysUser.getPassword()));
        }else {
            sysUser.setPassword(null);
        }
        sysUser.setUserType(USERTYPE);
        sysUser.setCustomerCompanyId(existUser.getCustomerCompanyId());
        sysUser.setLoginUser(existUser.getLoginUser());
        int result = authCommonService.updateByLoginUserAndCId(sysUser);
        if (result > 0) {
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
    @ApiOperation(value = "启用、暂停、删除账户接口  0 启用  1停用")
    @PostMapping("/changeStatus")
    public ResultMessage changeStatus(@RequestParam Integer userId,
                    @RequestParam
                    @Range(min = 0, max = 1, message = "status 参数错误") Integer status,
                    HttpServletRequest request) {

        SysUser existUser = authCommonService.findAllStatusUserById(userId);
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
    @ApiOperation(value = "查看账户信息接口")
    @GetMapping("/infoDetail")
    public ResultMessage infoDetail(@RequestParam(value = "userId") Integer userId) {
        SysUser detailUser = authCommonService.findUserById(userId);
        if (detailUser == null) {
            return ResultMessage.requestError("要查询的用户不存在：" + userId);
        } else {
            SysUserVO userVO = new SysUserVO();
            SysOrganization organization = authCommonService.findOrgInfoById(detailUser.getOrganizationId());
            SysOrganization parentOrg = authCommonService.findOrgInfoById(organization.getParentId());
            List<SysRoleSimpleVO> roleList = authCommonService.findRoleByUserId(userId);
            BeanUtils.copyProperties(detailUser, userVO);
            userVO.setOrganizationName(organization.getName());
            userVO.setRoleList(roleList);
            if(Objects.nonNull(parentOrg)){
                userVO.setParentOrgName(parentOrg.getName());
            }
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
    @ApiOperation(value = "更新账户密码信息接口")
    @PostMapping("/updatePwd")
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
        newUser.setCustomerCompanyId(currentUser.getCustomerCompanyId());
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(newUser, CreateUpdateEnum.UPDATE, accessIp);
        authCommonService.updateByLoginUserAndCId(newUser);
        return ResultMessage.success();
    }

    /**
     * 获取用户估价统计信息
     *
     * @return 统计信息
     */
    @ApiOperation(value = "个人估价统计信息")
    @GetMapping("/statistics")
    public ResultMessage userStatistics() {
        UserStatisticsVO statisticsVO = statisticsService.getUserStatistics();
        return ResultMessage.success(statisticsVO);
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public ResultMessage list(@ModelAttribute UserQuery query) {
        if(CollectionUtils.isEmpty(query.getOrganizationIds())){
            return ResultMessage.paramError("organizationIds");
        }
        query.setUserType(USERTYPE);
        PageInfo<SysUserVO> pageInfo = authCommonService.findUserList(query);
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
        if(currentUser == null) {
            return ResultMessage.apiError("用户查询失败").code(401);
        }
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
    @ApiOperation(value = "修改登录用户信息接口")
    @PostMapping("/login-user/edit")
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

        SysUser existUser = authCommonService.findUserByLoginUserAndCompanyId(loginUser,currentUser.getCustomerCompanyId());
        if (existUser == null) {
            return ResultMessage.requestError("要更新的用户不存在：" + loginUser);
        }
        SysUser sysUser = new SysUser();
        sysUser.setLoginUser(loginUser);
        sysUser.setEmail(email);
        sysUser.setMobilePhone(phone);
        sysUser.setUserType(USERTYPE);
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
    @ApiOperation(value = "获取登录用户的角色权限信息")
    @GetMapping("/login-user/perms")
    public ResultMessage loginUserRoleList() {
        SysUser sysUser = CommonUtil.getLoginUser();
        List<SysResource> resources = authCommonService.selectUserPermissionsByUserType(sysUser.getLoginUser(),sysUser.getUserType());
        return ResultMessage.success(resources);
    }

}
