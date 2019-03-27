package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.AssetQuery;
import cn.com.school.classinfo.common.query.EvalQuery;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.dto.excel.EvalRecordExportInfo;
import cn.com.school.classinfo.model.Asset;
import cn.com.school.classinfo.model.EvaluationRecord;
import cn.com.school.classinfo.service.AssetService;
import cn.com.school.classinfo.service.EvaluationService;
import cn.com.school.classinfo.utils.EvaluationUtil;
import cn.com.school.classinfo.utils.ExportUtil;
import cn.com.school.classinfo.vo.AssetVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author dongpp
 * @date 2018/10/29
 */
@Api(tags = "资产管理接口", position = 6)
@Slf4j
@Validated
@RestController
@RequestMapping("/api/asset")
public class AssetController {

    private final AvsConfig avsConfig;

    private final AssetService assetService;

    private final EvaluationService evaluationService;

    @Autowired
    public AssetController(AvsConfig avsConfig,
                           AssetService assetService,
                           EvaluationService evaluationService) {
        this.avsConfig = avsConfig;
        this.assetService = assetService;
        this.evaluationService = evaluationService;
    }

    /**
     * 增加资产管理
     * @param evalId 估价记录ID
     * @return 结果信息
     */
    @ApiOperation(value = "加入资产管理")
    @PostMapping("/add")
    public ResultMessage add(@ApiParam(value = "估价记录ID", required = true) @RequestParam Integer evalId){
        EvalQuery query = new EvalQuery();
        query.setEvalRecordId(evalId);
        //根据当前用户查询信息
        query.authInfo();
        EvaluationRecord record = evaluationService.getEvaluationRecord(query);
        if(Objects.isNull(record)){
            return ResultMessage.paramError("evalId");
        }
        //是否已经加入资产管理
        if(Objects.nonNull(record.getAsset()) && record.getAsset() == 1){
            return ResultMessage.requestError("资产管理中已存在该记录");
        }
        Asset asset = new Asset();
        asset.setEvalId(evalId);
        asset.setAssetType(record.getPropType());
        CommonUtil.doEvalCreateUpdateInfo(asset, CreateUpdateEnum.CREATE);
        assetService.save(asset);

        //更新估价记录资产状态
        record.setAsset(1);
        CommonUtil.doEvalCreateUpdateInfo(record, CreateUpdateEnum.UPDATE);
        evaluationService.updateAsset(record);

        return ResultMessage.success();
    }

    /**
     * 更新资产管理信息
     * @param id 资产管理ID
     * @param assetType 资产类型
     * @return 结果信息
     */
    @ApiOperation(value = "更新资产管理类型")
    @PostMapping("/update")
    public ResultMessage updateType(@ApiParam(value = "资产管理ID", required = true) @RequestParam Integer id,
                                    @NotBlank @ApiParam(value = "资产类型", required = true) @RequestParam String assetType){
        Asset asset = new Asset();
        asset.setId(id);
        asset.setAssetType(assetType);
        CommonUtil.doEvalCreateUpdateInfo(asset, CreateUpdateEnum.UPDATE);
        assetService.update(asset);

        return ResultMessage.success();
    }

    /**
     * 删除资产管理
     * @param id 资产管理ID
     * @return 结果信息
     */
    @ApiOperation(value = "删除资产管理")
    @PostMapping("/del")
    public ResultMessage del(@ApiParam(value = "资产管理ID", required = true) @RequestParam Integer id){
        if(Objects.isNull(id)){
            return ResultMessage.paramError("id");
        }
        AssetQuery query = new AssetQuery();
        query.setId(id);
        Asset asset = assetService.getById(query);
        if(Objects.isNull(asset)){
            return ResultMessage.paramError("id");
        }
        asset.setDel(1);
        CommonUtil.doEvalCreateUpdateInfo(asset, CreateUpdateEnum.UPDATE);
        assetService.update(asset);

        //更新估价记录资产状态
        EvaluationRecord record = new EvaluationRecord();
        record.setId(asset.getEvalId());
        record.setAsset(0);
        CommonUtil.doEvalCreateUpdateInfo(record, CreateUpdateEnum.UPDATE);
        evaluationService.updateAsset(record);
        return ResultMessage.success();
    }

    /**
     * 查询资产管理列表
     * @param query 查询条件
     * @return 资产管理列表
     */
    @ApiOperation(value = "资产管理列表")
    @GetMapping("/list")
    public ResultMessage list(@Valid @ModelAttribute AssetQuery query){
        if(StringUtils.isBlank(query.getType())){
            query.setType(null);
        }
        //根据当前用户查询信息
        query.authInfo();
        PageInfo<AssetVO> voList = assetService.getListByQuery(query);
        Map<String, Object> result = Maps.newHashMap();
        double totalAmount = 0;
        if (CollectionUtils.isNotEmpty(voList.getList())) {
            totalAmount = voList.getList().stream().filter(assetVO -> assetVO.getEvalType().equals(1)).mapToDouble(AssetVO::getTotalPrice).sum();
        }
        result.put("amount", NumberUtils.toScaledBigDecimal(totalAmount, 2, RoundingMode.HALF_UP));
        result.put("pageInfo", voList);
        return ResultMessage.success(result);
    }

    /**
     * 导出资产管理列表
     * @param query 查询条件
     * @return excel
     */
    @ApiOperation(value = "导出资产管理列表")
    @GetMapping("/export")
    public ResponseEntity exportList(@Valid @ModelAttribute AssetQuery query) {
        if(StringUtils.isBlank(query.getType())){
            query.setType(null);
        }
        //根据当前用户查询信息
        query.authInfo();
        //查询列表
        List<AssetVO> recordList = assetService.getAllListByQuery(query);
        if(CollectionUtils.isEmpty(recordList)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no results");
        }
        //转换导出对象
        List<EvalRecordExportInfo> exportInfoList = Lists.newArrayList();
        for (int i = 0; i < recordList.size(); i++) {
            AssetVO record = recordList.get(i);
            EvalRecordExportInfo exportInfo = new EvalRecordExportInfo();
            BeanUtils.copyProperties(record, exportInfo);
            exportInfo.setOrderNo(i + 1);
            exportInfo.setEvalCount(record.getHistoryCount());
            String priceStr = record.getEvalType() == 1 ? (record.getTotalPrice() + "万元")
                    : (record.getTotalPrice() + "元/月");
            exportInfo.setTotalPrice(priceStr);
            exportInfo.setReportType(EvaluationUtil.getReportType(record.getInquiry(), record.getAdvisory()));
            exportInfoList.add(exportInfo);
        }
        //导出excel
        String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp();
        return ExportUtil.getExportExcelResponse(tmpPath, "资产管理记录.xlsx",
                EvalRecordExportInfo.class, exportInfoList);
    }
}
