package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.BaseQuery;
import cn.com.school.classinfo.model.SysAssetType;
import cn.com.school.classinfo.service.AssetTypeService;
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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * 资产类型
 *
 * @author dongpp
 * @date 2018/11/20
 */
@Api(tags = "资产类型接口")
@Validated
@RestController
@RequestMapping("/api/asset-type")
public class AssetTypeController {

    private final AssetTypeService assetTypeService;

    public AssetTypeController(AssetTypeService assetTypeService) {
        this.assetTypeService = assetTypeService;
    }

    @ApiOperation(value = "增加资产类型")
    @PostMapping("/add")
    public ResultMessage add(@NotBlank(message = "name 不能为空")
                             @ApiParam(value = "资产类型名称", required = true)
                             @RequestParam String name,
                             @ApiParam(value = "资产类型备注")
                             @RequestParam(required = false) String remark) {

        SysAssetType type = assetTypeService.getByName(name);
        if(Objects.nonNull(type)){
            return ResultMessage.duplicateError("资产类型名称已存在");
        }
        SysAssetType assetType = new SysAssetType();
        assetType.setAssetName(name);
        assetType.setAssetRemark(remark);
        CommonUtil.doEvalCreateUpdateInfo(assetType, CreateUpdateEnum.CREATE);
        assetTypeService.add(assetType);
        return ResultMessage.success();
    }

    @ApiOperation(value = "更新资产类型")
    @PostMapping("/update")
    public ResultMessage update(@ApiParam(value = "资产类型ID", required = true)
                                @RequestParam Integer id,
                                @NotBlank(message = "name 不能为空")
                                @ApiParam(value = "资产类型名称", required = true)
                                @RequestParam String name,
                                @ApiParam(value = "资产类型备注")
                                @RequestParam(required = false) String remark) {

        SysAssetType type = assetTypeService.getByName(name);
        if(Objects.nonNull(type) && !type.getId().equals(id)){
            return ResultMessage.duplicateError("资产类型名称已存在");
        }

        SysAssetType assetType = new SysAssetType();
        assetType.setId(id);
        assetType.setAssetName(name);
        assetType.setAssetRemark(remark);

        CommonUtil.doEvalCreateUpdateInfo(assetType, CreateUpdateEnum.UPDATE);
        assetTypeService.save(assetType);
        return ResultMessage.success();
    }

    @ApiOperation(value = "删除资产类型")
    @PostMapping("/del")
    public ResultMessage del(@ApiParam(value = "资产类型ID") @RequestParam Integer id) {
        SysAssetType assetType = new SysAssetType();
        assetType.setId(id);
        assetType.setDel(1);

        CommonUtil.doEvalCreateUpdateInfo(assetType, CreateUpdateEnum.UPDATE);
        assetTypeService.delete(assetType);
        return ResultMessage.success();
    }

    @ApiOperation(value = "资产类型列表")
    @GetMapping("/list")
    public ResultMessage list() {
        List<SysAssetType> assetTypeList = assetTypeService.list();
        return ResultMessage.success(assetTypeList);
    }

    @ApiOperation(value = "资产类型列表（分页）")
    @GetMapping("/pageable")
    public ResultMessage pageable(@Valid @ModelAttribute BaseQuery query) {
        PageInfo<SysAssetType> assetTypeList = assetTypeService.pageable(query);
        return ResultMessage.success(assetTypeList);
    }


}
