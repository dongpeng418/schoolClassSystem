/**
 *
 */
package cn.com.school.classinfo.controller;

import java.util.List;

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
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.RoleQuery;
import cn.com.school.classinfo.model.SysRole;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
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
@Api(tags = "角色相关接口")
@Validated
@RestController
@RequestMapping("/api/auth/role")
public class SysRoleController {

    private final AuthCommonService authCommonService;

    //客户角色类型
    private final String CUSTOMERROLETYPE = "3";

    @Autowired
    public SysRoleController(AuthCommonService authCommonService) {
        this.authCommonService = authCommonService;
    }

    /**
     * 添加角色接口
     *
     * @param sysRole 角色信息
     * @return 失败与成功
     */
    @ApiOperation(value = "添加角色接口")
    @PostMapping("/add")
    public ResultMessage addRole(@Valid @RequestBody SysRole sysRole, HttpServletRequest request) {
        SysUser currentUser = CommonUtil.getLoginUser();
        sysRole.setCustomerCompanyId(currentUser.getCustomerCompanyId());
        SysRole existRole = authCommonService.findRoleInfoByName(sysRole);
        if(existRole != null){
            return ResultMessage.requestError("角色名称已存在：" + existRole.getName());
        }
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        sysRole.setOrganizationId(currentUser.getOrganizationId());
        sysRole.setCustomerCompanyId(currentUser.getCustomerCompanyId());
        sysRole.setRoleType(CUSTOMERROLETYPE);
        CommonUtil.doCreateUpdateInfo(sysRole, CreateUpdateEnum.CREATE, accessIp);
        boolean result = authCommonService.addUserRoleInfo(sysRole);
        if(result) {
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
    @ApiOperation(value = "修改角色信息接口")
    @PostMapping("/edit")
    public ResultMessage editRole(@Valid @RequestBody SysRole sysRole, HttpServletRequest request) {
        SysRole existRole = authCommonService.findRoleInfoById(sysRole.getId());
        if(existRole == null){
            return ResultMessage.requestError("要修改的角色不存在：" + sysRole.getName());
        }
        SysUser currentUser = CommonUtil.getLoginUser();
        sysRole.setRoleType(CUSTOMERROLETYPE);
        sysRole.setCustomerCompanyId(currentUser.getCustomerCompanyId());
        existRole = authCommonService.findRoleInfoByName(sysRole);
        if(existRole != null && sysRole.getId()!=existRole.getId()){
            return ResultMessage.requestError("角色名称已存在：" + existRole.getName());
        }
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysRole, CreateUpdateEnum.UPDATE, accessIp);
        sysRole.setTenantId(currentUser.getTenantId());
        boolean result = authCommonService.editUserRoleInfo(sysRole);
        if(result) {
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
    @ApiOperation(value = "删除角色信息接口")
    @PostMapping("/delete")
    public ResultMessage deleteRole(@RequestParam(value = "roleId") Integer roleId) {
        boolean isSuccess = authCommonService.deleteRoleInfoById(roleId);
        if(isSuccess) {
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
    @ApiOperation(value = "查询角色信息接口（名称）")
    @GetMapping("/infodetail")
    public ResultMessage infoDetail(@NotBlank(message = "roleName 不能为空") @RequestParam(value = "roleName") String roleName) {
        SysRole conditionRole = new SysRole();
        SysUser currentUser = CommonUtil.getLoginUser();
        conditionRole.setName(roleName);
        conditionRole.setRoleType(CUSTOMERROLETYPE);
        conditionRole.setCustomerCompanyId(currentUser.getCustomerCompanyId());
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
    @ApiOperation(value = "查询角色信息接口（ID）")
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
    @ApiOperation(value = "查询角色列表-分页")
    @GetMapping("/pageable")
    public ResultMessage pageable(@Valid @ModelAttribute RoleQuery query) {
        SysUser currentUser = CommonUtil.getLoginUser();
        query.setRoleType(CUSTOMERROLETYPE);
        query.setCustomerCompanyId(currentUser.getCustomerCompanyId());
        PageInfo<SysRoleVO> roleList = authCommonService.findRoleList(query);
        return ResultMessage.success(roleList);
    }

    /**
     * 查询角色列表
     *
     * @return 角色列表
     */
    @ApiOperation(value = "查询角色列表-全部")
    @GetMapping("/list")
    public ResultMessage list() {
        RoleQuery query = new RoleQuery();
        SysUser currentUser = CommonUtil.getLoginUser();
        query.setRoleType(CUSTOMERROLETYPE);
        query.setCustomerCompanyId(currentUser.getCustomerCompanyId());
        List<SysRoleSimpleVO> roleList = authCommonService.findAllRoleByCondition(query);
        return ResultMessage.success(roleList);
    }

}
