/**
 *
 */
package cn.com.school.classinfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.model.SysPermission;
import cn.com.school.classinfo.service.AuthCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dongpp
 *
 */
@Api(tags = "权限相关接口")
@RestController
@RequestMapping("/api/tenant/auth/permission")
public class TenantSysPermissionController {

    private final AuthCommonService authCommonService;

    @Autowired
    public TenantSysPermissionController(AuthCommonService authCommonService) {
        this.authCommonService = authCommonService;
    }

    /**
     * 获取角色拥有的权限
     * @return 是否成功
     */
    @ApiOperation(value = "【1、渠道角色，2：客户角色】获取角色拥有的权限通用")
    @GetMapping("/fRolePermissions")
    public ResultMessage fRolePermissions(@RequestParam(value = "roleId") Integer roleId){
        List<SysPermission> permissions = authCommonService.selectPermissionsByRoleId(roleId);
        return ResultMessage.success(permissions);
    }

}
