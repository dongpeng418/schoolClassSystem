/**
 *
 */
package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.model.SysResource;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.AuthCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dongpp
 * 渠道客户，系统权限设置相关
 */
@Api(tags = "渠道-资源相关接口")
@RestController
@RequestMapping("/api/tenant/auth/resource")
public class TenantSysResourceController {

    private final AuthCommonService authCommonService;

    //渠道资源类别
    private final int RESTYPE = 1;

    //客户资源类别
    private final int CUSTOMERRESTYPE = 2;

    @Autowired
    public TenantSysResourceController(AuthCommonService authCommonService) {
        this.authCommonService = authCommonService;
    }

    @ApiOperation(value = "渠道用户_需要分配的系统权限")
    @GetMapping("/t/distributiones")
    public ResultMessage distributiones(){
        List<SysResource> reses = authCommonService.selectNeedDistributeReses(RESTYPE);
        if(reses.size() == 0) {
            return ResultMessage.requestError("系统无待分配权限资源");
        }
        return ResultMessage.success(reses);
    }

    @ApiOperation(value = "渠道给客户用户分配权限")
    @GetMapping("/t/c/distributiones")
    public ResultMessage chCustomerUserDistributiones(){
        List<SysResource> reses = authCommonService.selectNeedDistributeReses(CUSTOMERRESTYPE);
        if(reses.size() == 0) {
            return ResultMessage.requestError("系统无待分配权限资源");
        }
        return ResultMessage.success(reses);
    }

    @ApiOperation(value = "渠道客户用户【是在自身拥有的权限基础上进行分配，而不是全集权限】_需要分配的系统权限")
    @GetMapping("/c/distributiones")
    public ResultMessage customerUserDistributiones(){
        SysUser sysUser = CommonUtil.getLoginUser();
        int userType = sysUser.getUserType();
        List<SysResource> reses = authCommonService.selectCustomerUserCanPermissions(sysUser.getLoginUser(),userType);
        if(reses.size() == 0) {
            return ResultMessage.requestError("系统无待分配权限资源");
        }
        return ResultMessage.success(reses);
    }

}
