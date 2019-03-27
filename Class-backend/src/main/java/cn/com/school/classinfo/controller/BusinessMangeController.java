package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.BaseQuery;
import cn.com.school.classinfo.model.SysBusinessManage;
import cn.com.school.classinfo.service.BusinessMangeService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * 业务管理
 *
 * @author dongpp
 * @date 2018/11/20
 */
@Api(tags = "业务管理接口", position = 15)
@Validated
@RestController
@RequestMapping("/api/business")
public class BusinessMangeController {

    private final BusinessMangeService businessMangeService;

    public BusinessMangeController(BusinessMangeService businessMangeService) {
        this.businessMangeService = businessMangeService;
    }

    @ApiOperation(value = "增加业务")
    @PostMapping("/add")
    public ResultMessage add(@ApiParam(value = "业务名称", required = true)
                             @NotBlank(message = "业务名称不能为空")
                             @RequestParam String name,
                             @ApiParam(value = "业务备注") @RequestParam(required = false) String remark) {

        SysBusinessManage dbManage = businessMangeService.getByName(name);
        if(Objects.nonNull(dbManage)){
            return ResultMessage.duplicateError("业务名称已存在");
        }

        SysBusinessManage manage = new SysBusinessManage();
        manage.setBusinessName(name);
        manage.setBusinessRemark(remark);

        CommonUtil.doEvalCreateUpdateInfo(manage, CreateUpdateEnum.CREATE);
        businessMangeService.add(manage);
        return ResultMessage.success();
    }

    @ApiOperation(value = "更新业务")
    @PostMapping("/update")
    public ResultMessage update(@ApiParam(value = "业务ID", required = true) @RequestParam Integer id,
                                @NotBlank(message = "业务名称不能为空")
                                @ApiParam(value = "业务名称", required = true)
                                @RequestParam String name,
                                @ApiParam(value = "业务备注") @RequestParam(required = false) String remark) {

        SysBusinessManage dbManage = businessMangeService.getByName(name);
        if(Objects.nonNull(dbManage) && !id.equals(dbManage.getId())){
            return ResultMessage.duplicateError("业务名称已存在");
        }

        SysBusinessManage manage = new SysBusinessManage();
        manage.setId(id);
        manage.setBusinessName(name);
        manage.setBusinessRemark(remark);

        CommonUtil.doEvalCreateUpdateInfo(manage, CreateUpdateEnum.UPDATE);
        businessMangeService.save(manage);
        return ResultMessage.success();
    }

    @ApiOperation(value = "删除业务")
    @PostMapping("/del")
    public ResultMessage del(@ApiParam(value = "业务ID") @RequestParam Integer id) {
        SysBusinessManage manage = new SysBusinessManage();
        manage.setId(id);
        manage.setDel(1);

        CommonUtil.doEvalCreateUpdateInfo(manage, CreateUpdateEnum.UPDATE);
        businessMangeService.delete(manage);
        return ResultMessage.success();
    }

    @ApiOperation(value = "业务列表")
    @GetMapping("/list")
    public ResultMessage list() {
        List<SysBusinessManage> manageList = businessMangeService.list();
        return ResultMessage.success(manageList);
    }

    @ApiOperation(value = "业务列表（分页）")
    @GetMapping("/pageable")
    public ResultMessage pageable(@ModelAttribute BaseQuery query) {
        PageInfo<SysBusinessManage> manageList = businessMangeService.pageable(query);
        return ResultMessage.success(manageList);
    }

}
