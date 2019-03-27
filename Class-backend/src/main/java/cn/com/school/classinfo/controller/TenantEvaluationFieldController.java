package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.CommonConstant;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.EvalFieldQuery;
import cn.com.school.classinfo.model.SysEvaluationField;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.service.EvaluationFieldService;
import cn.com.school.classinfo.service.SysLogService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.utils.ObjectUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 估价字段配置接口
 *
 * @author dongpp
 * @date 2018/11/21
 */
@Api(tags = "渠道-估价字段配置接口")
@Validated
@RestController
@RequestMapping("/api/tenant/eval-field")
public class TenantEvaluationFieldController {

    private final EvaluationFieldService evaluationFieldService;

    private final SysLogService sysLogService;

    public TenantEvaluationFieldController(EvaluationFieldService evaluationFieldService,
                                           SysLogService sysLogService) {
        this.evaluationFieldService = evaluationFieldService;
        this.sysLogService = sysLogService;
    }

    @ApiOperation(value = "保存估价字段配置")
    @PostMapping("/save")
    @SuppressWarnings("unchecked")
    public ResultMessage save(@ApiParam(value = "估价字段列表", required = true)
                              @Valid @RequestBody List<SysEvaluationField> fields,
                              HttpServletRequest request) {
        if (CollectionUtils.isEmpty(fields)) {
            return ResultMessage.paramError();
        }

        List<SysEvaluationField> oldList = (List<SysEvaluationField>) list().getData();

        fields.forEach(field -> CommonUtil.doEvalCreateUpdateInfo(field, CreateUpdateEnum.UPDATE));
        evaluationFieldService.save(fields);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        StringBuilder modifyDetails = new StringBuilder();
        for(int i = 0; i < fields.size(); i++){
            String modifyDetail = ObjectUtil.getModifyInfo(fields.get(i), oldList.get(i));
            modifyDetails.append(modifyDetail);
        }
        sysLog.setOperateLog(String.format(SysLogConstant.TENANT_FIELD_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                DateUtil.nowToString(), modifyDetails, HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    @ApiOperation(value = "估价字段配置列表")
    @GetMapping("/list")
    public ResultMessage list() {
        EvalFieldQuery query = new EvalFieldQuery();
        query.setType(CommonConstant.TENANT_TYPE);
        query.setTenantId(CommonUtil.getTenantId());
        List<SysEvaluationField> fields = evaluationFieldService.list(query);
        return ResultMessage.success(fields);
    }
}
