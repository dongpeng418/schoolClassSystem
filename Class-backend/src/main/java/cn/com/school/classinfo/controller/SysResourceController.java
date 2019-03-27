/**
 *
 */
package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.model.SysResource;
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
 *
 */
@Api(tags = "资源相关接口")
@RestController
@RequestMapping("/api/auth/resource")
public class SysResourceController {

    private final AuthCommonService authCommonService;

    private final int RESTYPE = 2;

    @Autowired
    public SysResourceController(AuthCommonService authCommonService) {
        this.authCommonService = authCommonService;
    }

    @ApiOperation(value = "需要分配的系统权限")
    @GetMapping("/distributiones")
    public ResultMessage distributiones(){
        List<SysResource> reses = authCommonService.selectNeedDistributeReses(RESTYPE);
        if(reses.size() == 0) {
            return ResultMessage.requestError("系统无待分配权限资源");
        }
        return ResultMessage.success(reses);
    }

}
