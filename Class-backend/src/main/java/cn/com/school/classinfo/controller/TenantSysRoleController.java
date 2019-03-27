/**
 *
 */
package cn.com.school.classinfo.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.RoleQuery;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.model.SysRole;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.service.SysLogService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.vo.SysRoleSimpleVO;
import cn.com.school.classinfo.vo.SysRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dongpp
 * 系统权限管理_角色管理
 *
 */
@Api(tags = "渠道-角色相关接口")
@Validated
@RestController
@RequestMapping("/api/tenant/auth/role")
public class TenantSysRoleController {

    private final AuthCommonService authCommonService;

    //渠道角色类型
    private final String TENANTROLETYPE = "1";

    //客户用户角色类型
    private final String CUSERROLETYPE = "2";

    private final SysLogService sysLogService;

    @Autowired
    public TenantSysRoleController(AuthCommonService authCommonService,
                    SysLogService sysLogService) {
        this.authCommonService = authCommonService;
        this.sysLogService = sysLogService;
    }

    /**
     * 添加角色接口
     *
     * @param sysRole 角色信息
     * @return 失败与成功
     */
    @ApiOperation(value = "添加渠道角色角色接口")
    @PostMapping("/t/add")
    public ResultMessage addRole(@Valid @RequestBody SysRole sysRole, HttpServletRequest request) {
        SysUser currentUser = CommonUtil.getLoginUser();
        sysRole.setTenantId(currentUser.getTenantId());
        sysRole.setRoleType(TENANTROLETYPE);
        SysRole existRole = authCommonService.findRoleInfoByName(sysRole);
        if(existRole != null){
            return ResultMessage.requestError("角色名称已存在：" + existRole.getName());
        }
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        sysRole.setOrganizationId(currentUser.getOrganizationId());

        CommonUtil.doCreateUpdateInfo(sysRole, CreateUpdateEnum.CREATE, accessIp);
        boolean result = authCommonService.addUserRoleInfo(sysRole);
        if(result) {
            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            sysLog.setOperateLog(String.format(SysLogConstant.TENANT_ROLE_ADD, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), sysRole.getName(), HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);
            return ResultMessage.success();
        }else {
            return ResultMessage.requestError("角色信息添加失败!");
        }

    }

    /**
     * 添加角色接口
     *
     * @param sysRole 角色信息
     * @return 失败与成功
     */
    @ApiOperation(value = "添加渠道客户角色接口")
    @PostMapping("/c/add")
    public ResultMessage addCustomerRole(@Valid @RequestBody SysRole sysRole, HttpServletRequest request) {
        SysUser currentUser = CommonUtil.getLoginUser();
        sysRole.setTenantId(currentUser.getTenantId());
        sysRole.setRoleType(CUSERROLETYPE);
        SysRole existRole = authCommonService.findRoleInfoByName(sysRole);
        if(existRole != null){
            return ResultMessage.requestError("角色名称已存在：" + existRole.getName());
        }
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        sysRole.setOrganizationId(currentUser.getOrganizationId());
        CommonUtil.doCreateUpdateInfo(sysRole, CreateUpdateEnum.CREATE, accessIp);
        boolean result = authCommonService.addUserRoleInfo(sysRole);
        if(result) {
            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            sysLog.setOperateLog(String.format(SysLogConstant.CUSTOMER_ROLE_ADD, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), sysRole.getName(), HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);
            return ResultMessage.success();
        }else {
            return ResultMessage.requestError("角色信息添加失败!");
        }

    }


    /**
     * 修改角色信息接口
     *
     * @param sysRole 角色信息
     * @return 失败与成功
     */
    @ApiOperation(value = "修改渠道角色信息接口")
    @PostMapping("/t/edit")
    public ResultMessage editRole(@Valid @RequestBody SysRole sysRole, HttpServletRequest request) {
        SysRole existRole = authCommonService.findRoleInfoById(sysRole.getId());
        if(existRole == null){
            return ResultMessage.requestError("要修改的角色不存在：" + sysRole.getName());
        }

        SysUser currentUser = CommonUtil.getLoginUser();
        sysRole.setRoleType(TENANTROLETYPE);
        currentUser.setOrganizationId(currentUser.getOrganizationId());
        existRole = authCommonService.findRoleInfoByName(sysRole);
        if(existRole != null && !sysRole.getId().equals(existRole.getId())){
            return ResultMessage.requestError("角色名称已存在：" + existRole.getName());
        }
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysRole, CreateUpdateEnum.UPDATE, accessIp);
        currentUser.setTenantId(sysRole.getTenantId());
        boolean result = authCommonService.editUserRoleInfo(sysRole);
        if(result) {
            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            sysLog.setOperateLog(String.format(SysLogConstant.TENANT_ROLE_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), sysRole.getName(), HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);
            return ResultMessage.success();
        }else {
            return ResultMessage.requestError("角色信息更新失败!");
        }

    }

    /**
     * 修改角色信息接口
     *
     * @param sysRole 角色信息
     * @return 失败与成功
     */
    @ApiOperation(value = "修改渠道客户角色信息接口")
    @PostMapping("/c/edit")
    public ResultMessage editCustomerRole(@Valid @RequestBody SysRole sysRole, HttpServletRequest request) {
        SysRole existRole = authCommonService.findRoleInfoById(sysRole.getId());
        if(existRole == null){
            return ResultMessage.requestError("要修改的角色不存在：" + sysRole.getName());
        }

        SysUser currentUser = CommonUtil.getLoginUser();
        sysRole.setRoleType(CUSERROLETYPE);
        currentUser.setOrganizationId(currentUser.getOrganizationId());
        existRole = authCommonService.findRoleInfoByName(sysRole);
        if(existRole != null && !sysRole.getId().equals(existRole.getId())){
            return ResultMessage.requestError("角色名称已存在：" + existRole.getName());
        }
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysRole, CreateUpdateEnum.UPDATE, accessIp);
        sysRole.setTenantId(sysRole.getTenantId());
        boolean result = authCommonService.editUserRoleInfo(sysRole);
        if(result) {
            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            sysLog.setOperateLog(String.format(SysLogConstant.CUSTOMER_ROLE_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), sysRole.getName(), HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);
            return ResultMessage.success();
        }else {
            return ResultMessage.requestError("角色信息更新失败!");
        }

    }

    /**
     * 删除角色信息接口
     *
     * @param roleId 角色ID
     * @return 失败与成功
     */
    @ApiOperation(value = "删除角色信息接口，【渠道角色、渠道用户角色均适用】")
    @PostMapping("/delete")
    public ResultMessage deleteRole(@RequestParam(value = "roleId") Integer roleId, HttpServletRequest request) {
        SysRole role = authCommonService.findRoleInfoById(roleId);
        if(Objects.isNull(role)){
            return ResultMessage.paramError("id");
        }
        boolean isSuccess = authCommonService.deleteRoleInfoById(roleId);
        if(isSuccess) {
            //系统日志
            SysLog sysLog = new SysLog();
            CommonUtil.doOperateLogInfo(sysLog);
            String logStr;
            if(TENANTROLETYPE.equals(role.getRoleType())){
                logStr = SysLogConstant.TENANT_ROLE_DELETE;
            }else {
                logStr = SysLogConstant.CUSTOMER_ROLE_DELETE;
            }
            sysLog.setOperateLog(String.format(logStr, CommonUtil.getLoginUser().getLoginUser(),
                            DateUtil.nowToString(), role.getName(), HttpRequestUtil.getRemoteIp(request)));
            sysLogService.add(sysLog);
            return ResultMessage.success();
        }else {
            return ResultMessage.requestError("角色信息删除失败!");
        }
    }

    /**
     * 查询角色信息接口
     *
     * @param roleName 角色名称
     * @return 失败与成功
     */
    @ApiOperation(value = "查询渠道角色信息接口（名称）")
    @GetMapping("/t/infodetail")
    public ResultMessage infoDetail(@NotBlank(message = "roleName 不能为空") @RequestParam(value = "roleName") String roleName) {
        SysUser currentUser = CommonUtil.getLoginUser();

        SysRole conditionRole = new SysRole();
        conditionRole.setName(roleName);
        conditionRole.setRoleType(TENANTROLETYPE);
        conditionRole.setOrganizationId(currentUser.getOrganizationId());
        SysRole sysRole = authCommonService.findRoleInfoByName(conditionRole);
        if(sysRole!=null) {
            return ResultMessage.success(sysRole);
        }else {
            return ResultMessage.requestError("角色信息查询失败!");
        }
    }

    /**
     * 查询角色信息接口
     *
     * @param roleName 角色名称
     * @return 失败与成功
     */
    @ApiOperation(value = "查询渠道客户角色信息接口（名称）")
    @GetMapping("/c/infodetail")
    public ResultMessage customerInfoDetail(@NotBlank(message = "roleName 不能为空") @RequestParam(value = "roleName") String roleName) {
        SysUser currentUser = CommonUtil.getLoginUser();

        SysRole conditionRole = new SysRole();
        conditionRole.setName(roleName);
        conditionRole.setRoleType(CUSERROLETYPE);
        conditionRole.setOrganizationId(currentUser.getOrganizationId());
        SysRole sysRole = authCommonService.findRoleInfoByName(conditionRole);
        if(sysRole!=null) {
            return ResultMessage.success(sysRole);
        }else {
            return ResultMessage.requestError("角色信息查询失败!");
        }
    }

    /**
     * 查询角色信息接口
     *
     * @param roleId 角色ID
     * @return 失败与成功
     */
    @ApiOperation(value = "查询角色信息接口（ID）,【渠道角色、客户角色均适用】")
    @GetMapping("/infodetailById")
    public ResultMessage infoDetailById(@RequestParam(value = "roleId") Integer roleId) {
        SysRole sysRole = authCommonService.findRoleInfoById(roleId);
        if(sysRole!=null) {
            return ResultMessage.success(sysRole);
        }else {
            return ResultMessage.requestError("角色信息查询失败!");
        }
    }

    /**
     * 查询角色列表-分页
     *
     * @return 角色列表
     */
    @ApiOperation(value = "查询渠道角色列表-分页")
    @GetMapping("/t/pageable")
    public ResultMessage pageable(@Valid @ModelAttribute RoleQuery query) {
        SysUser currentUser = CommonUtil.getLoginUser();
        query.setRoleType(TENANTROLETYPE);
        //query.setOrganizationId(currentUser.getOrganizationId());
        query.setTenantId(currentUser.getTenantId());
        PageInfo<SysRoleVO> roleList = authCommonService.findRoleList(query);
        return ResultMessage.success(roleList);
    }

    /**
     * 查询角色列表-分页
     *
     * @return 角色列表
     */
    @ApiOperation(value = "查询渠道客户角色列表-分页")
    @GetMapping("/c/pageable")
    public ResultMessage customerPageable(@Valid @ModelAttribute RoleQuery query) {
        SysUser currentUser = CommonUtil.getLoginUser();
        query.setRoleType(CUSERROLETYPE);
        query.setOrganizationId(currentUser.getOrganizationId());
        PageInfo<SysRoleVO> roleList = authCommonService.findRoleList(query);
        return ResultMessage.success(roleList);
    }

    /**
     * 查询角色列表-分页
     *
     * @return 角色列表
     */
    @ApiOperation(value = "查询渠道下的所有客户角色-分页")
    @GetMapping("/t/c/pageable")
    public ResultMessage chCustomerPageable(@Valid @ModelAttribute RoleQuery query) {
        SysUser currentUser = CommonUtil.getLoginUser();
        query.setTenantId(currentUser.getTenantId());
        query.setRoleType(CUSERROLETYPE);
        PageInfo<SysRoleVO> roleList = authCommonService.findRoleListByTenantId(query);
        return ResultMessage.success(roleList);
    }

    /**
     * 查询角色列表
     *
     * @return 角色列表
     */
    @ApiOperation(value = "查询渠道角色列表，分页-全部")
    @GetMapping("/t/list")
    public ResultMessage list() {
        SysUser currentUser = CommonUtil.getLoginUser();
        RoleQuery query = new RoleQuery();
        query.setRoleType(TENANTROLETYPE);
        /*query.setOrganizationId(currentUser.getOrganizationId());*/
        query.setTenantId(currentUser.getTenantId());
        List<SysRoleSimpleVO> roleList = authCommonService.findAllRoleByCondition(query);
        return ResultMessage.success(roleList);
    }

    /**
     * 查询渠道角色列表
     *
     * @return 角色列表
     */
    @ApiOperation(value = "查询渠道客户角色列表，分页-全部")
    @GetMapping("/c/list")
    public ResultMessage customerList() {
        SysUser currentUser = CommonUtil.getLoginUser();
        RoleQuery query = new RoleQuery();
        query.setRoleType(CUSERROLETYPE);
        query.setTenantId(currentUser.getTenantId());
        List<SysRoleSimpleVO> roleList = authCommonService.findRoleListByTenantIdNoPage(query);
        return ResultMessage.success(roleList);
    }

}
