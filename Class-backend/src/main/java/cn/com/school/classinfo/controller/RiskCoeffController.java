package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.RiskQuery;
import cn.com.school.classinfo.model.SysRiskCoefficient;
import cn.com.school.classinfo.service.RiskCoefficientService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * 风控系数接口
 *
 * @author dongpp
 * @date 2018/11/20
 */
@Api(tags = "风控系数接口")
@Validated
@RestController
@RequestMapping("/api/risk")
public class RiskCoeffController {

    private final RiskCoefficientService riskCoefficientService;

    public RiskCoeffController(RiskCoefficientService riskCoefficientService) {
        this.riskCoefficientService = riskCoefficientService;
    }

    @ApiOperation(value = "增加风控系数")
    @PostMapping("/add")
    public ResultMessage add(@Valid @ModelAttribute SysRiskCoefficient coefficient) {

        RiskQuery query = new RiskQuery();
        BeanUtils.copyProperties(coefficient, query);
        query.authInfo();
        SysRiskCoefficient old = riskCoefficientService.getByRegionQuery(query);
        if(Objects.nonNull(old)){
            return ResultMessage.duplicateError("相关区域风险系数已存在");
        }
        CommonUtil.doEvalCreateUpdateInfo(coefficient, CreateUpdateEnum.CREATE);
        riskCoefficientService.add(coefficient);
        return ResultMessage.success();
    }

    @ApiOperation(value = "更新风控系数")
    @PostMapping("/update")
    public ResultMessage update(@Valid @ModelAttribute SysRiskCoefficient coefficient) {
        RiskQuery query = new RiskQuery();
        BeanUtils.copyProperties(coefficient, query);
        query.authInfo();
        SysRiskCoefficient old = riskCoefficientService.getByRegionQuery(query);
        if(Objects.nonNull(old) && !coefficient.getId().equals(old.getId())){
            return ResultMessage.duplicateError("相关区域风险系数已存在");
        }
        CommonUtil.doEvalCreateUpdateInfo(coefficient, CreateUpdateEnum.UPDATE);
        riskCoefficientService.save(coefficient);
        return ResultMessage.success();
    }

    @ApiOperation(value = "删除风控系数")
    @PostMapping("/del")
    public ResultMessage del(@ApiParam(value = "风控系数ID") @RequestParam Integer id) {
        if (Objects.isNull(id)) {
            return ResultMessage.paramError();
        }
        SysRiskCoefficient coefficient = new SysRiskCoefficient();
        coefficient.setId(id);
        CommonUtil.doEvalCreateUpdateInfo(coefficient, CreateUpdateEnum.UPDATE);
        riskCoefficientService.delete(coefficient);
        return ResultMessage.success();
    }

    @ApiOperation(value = "风控系数列表")
    @GetMapping("/list")
    public ResultMessage list(@Valid @ModelAttribute RiskQuery query) {
        query.authInfo();
        PageInfo<SysRiskCoefficient> coefficientList = riskCoefficientService.list(query);
        return ResultMessage.success(coefficientList);
    }

    @ApiOperation(value = "获取相关区域的风险系数")
    @GetMapping("/get")
    public ResultMessage get(@ApiParam(value = "省编码", required = true)
                                @NotBlank(message = "省编码 不能为空")
                                @RequestParam String provinceCode,
                            @ApiParam(value = "城市编码")
                                @RequestParam(required = false) String cityCode,
                            @ApiParam(value = "区县编码")
                                @RequestParam(required = false) String distCode) {
        RiskQuery query = new RiskQuery();
        query.setProvinceCode(provinceCode);
        query.setCityCode(StringUtils.isBlank(cityCode) ? null : cityCode);
        query.setDistCode(StringUtils.isBlank(distCode) ? null : distCode);
        query.authInfo();
        SysRiskCoefficient coefficient = riskCoefficientService.getByRegionQuery(query);
        return ResultMessage.success(coefficient);
    }
}
