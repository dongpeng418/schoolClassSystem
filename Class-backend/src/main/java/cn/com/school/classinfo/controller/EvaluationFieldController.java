package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.CommonConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.EvalFieldQuery;
import cn.com.school.classinfo.model.SysEvaluationField;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.EvaluationFieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 估价字段配置接口
 *
 * @author dongpp
 * @date 2018/11/21
 */
@Api(tags = "估价字段配置接口")
@Validated
@RestController
@RequestMapping("/api/eval-field")
public class EvaluationFieldController {

    private final EvaluationFieldService evaluationFieldService;

    public EvaluationFieldController(EvaluationFieldService evaluationFieldService) {
        this.evaluationFieldService = evaluationFieldService;
    }

    @ApiOperation(value = "保存估价字段配置")
    @PostMapping("/save")
    public ResultMessage save(@ApiParam(value = "估价字段列表", required = true)
                              @Valid @RequestBody List<SysEvaluationField> fields) {
        if (CollectionUtils.isEmpty(fields)) {
            return ResultMessage.paramError();
        }
        fields.forEach(field -> CommonUtil.doEvalCreateUpdateInfo(field, CreateUpdateEnum.UPDATE));
        evaluationFieldService.save(fields);
        return ResultMessage.success();
    }

    @ApiOperation(value = "估价字段配置列表")
    @GetMapping("/list")
    public ResultMessage list() {
        EvalFieldQuery query = new EvalFieldQuery();
        SysUser sysUser = CommonUtil.getLoginUser();
        query.setType(CommonConstant.CUSTOMER_TYPE);
        query.setTenantId(sysUser.getTenantId());
        query.setCompanyId(sysUser.getCustomerCompanyId());
        List<SysEvaluationField> fields = evaluationFieldService.list(query);
        return ResultMessage.success(fields);
    }
}
