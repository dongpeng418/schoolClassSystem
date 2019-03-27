package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.Pair;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.EvalConstant;
import cn.com.school.classinfo.common.constant.ReportConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.EvalQuery;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.dto.HaDetail;
import cn.com.school.classinfo.dto.HaSimpleInfo;
import cn.com.school.classinfo.dto.HouseInfo;
import cn.com.school.classinfo.dto.excel.EvalRecordExportInfo;
import cn.com.school.classinfo.model.Dict;
import cn.com.school.classinfo.model.EvaluationBeta;
import cn.com.school.classinfo.model.EvaluationRecord;
import cn.com.school.classinfo.model.EvaluationRecordHistory;
import cn.com.school.classinfo.model.EvaluationReport;
import cn.com.school.classinfo.model.HouseResourceSample;
import cn.com.school.classinfo.service.DictService;
import cn.com.school.classinfo.service.EvaluationService;
import cn.com.school.classinfo.service.SysReportConfigService;
import cn.com.school.classinfo.service.TransferApiService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.EvaluationUtil;
import cn.com.school.classinfo.utils.ExportUtil;
import cn.com.school.classinfo.utils.FileUtil;
import cn.com.school.classinfo.utils.FreeMarkerUtil;
import cn.com.school.classinfo.utils.JsonUtil;
import cn.com.school.classinfo.utils.PdfUtil;
import cn.com.school.classinfo.utils.RegionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 估价Controller
 *
 * @author dongpp
 * @date 2018/10/23
 */
@Api(tags = "房产估价相关接口", position = 4)
@Slf4j
@Validated
@RestController
@RequestMapping("/api/eval")
public class EvaluationController {

    private final AvsConfig avsConfig;

    private final EvaluationService evaluationService;

    private final TransferApiService transferApiService;

    private final DictService dictService;

    private final SysReportConfigService sysReportConfigService;

    @Autowired
    public EvaluationController(AvsConfig avsConfig,
                                EvaluationService evaluationService,
                                TransferApiService transferApiService,
                                DictService dictService,
                                SysReportConfigService sysReportConfigService) {
        this.avsConfig = avsConfig;
        this.evaluationService = evaluationService;
        this.transferApiService = transferApiService;
        this.dictService = dictService;
        this.sysReportConfigService = sysReportConfigService;
    }

    @PostConstruct
    public void copyReportFile(){
        URL url = this.getClass().getClassLoader().getResource("template");
        try {
            if(Objects.nonNull(url)){
                log.info("[evaluation] report template class path: {}", url);
                String path = avsConfig.getPath().getBase() + avsConfig.getPath().getReport() + "template";
                File file = new File(path);
                FileUtils.copyDirectory(new File(url.getFile()), file);
            }
        } catch (IOException e) {
            log.error("[evaluation] copy report template file error.", e);
        }
    }

    /**
     * 快速估价接口
     *
     * @param houseInfo 房屋信息
     * @param evalType      估价方法
     * @return 估价结果
     */
    @ApiOperation(value = "快速估价接口")
    @PostMapping("/rapid")
    public ResultMessage rapid(@Valid @RequestBody HouseInfo houseInfo,
                               @ApiParam(value = "估价类型，1：售价，2：租金")
                               @Pattern(regexp = "[1,2]", message = "evalType参数错误")
                               @RequestParam(defaultValue = "1") String evalType) {

        //如果城市码或城市名称为空，则根据不为空的城市码或名来获取为空的
        if(StringUtils.isEmpty(houseInfo.getCityName())){
            houseInfo.setCityName(RegionUtil.getCityName(houseInfo.getCityCode()));
        }

        if(StringUtils.isEmpty(houseInfo.getCityCode())){
            houseInfo.setCityCode(RegionUtil.getCityCode(houseInfo.getCityName()));
        }

        //如果有GPS信息，刚以GPS信息为准进行估价，去除小区信息
        HaSimpleInfo haSimpleInfo = houseInfo.getHaInfo();
        if(Objects.nonNull(houseInfo.getGpsInfo())){
            houseInfo.setHaInfo(null);
        }
        //快速估价接口
        Pair<Boolean, String> result = transferApiService.rapid(houseInfo, evalType);
        //获取小区地址信息
        if(Objects.nonNull(houseInfo.getHaInfo())){
            Pair<Boolean, String> haDetailPair = transferApiService.getHaDetail(houseInfo.getHaInfo().getHaCode(),
                    houseInfo.getCityCode());
            if(haDetailPair.getLeft()){
                JsonNode rootNode = JsonUtil.readTree(haDetailPair.getRight());
                String distCode = JsonUtil.stringValue(rootNode.get("distCode"));
                String distName = JsonUtil.stringValue(rootNode.get("distName"));
                String streetName = JsonUtil.stringValue(rootNode.get("streetName"));
                String streetNo = JsonUtil.stringValue(rootNode.get("streetNo"));
                String location = houseInfo.getCityName() + distName + streetName + streetNo;
                houseInfo.setLocation(location);
                houseInfo.setDistCode(distCode);
                houseInfo.setDistName(distName);
            }
        }
        //保存估价结果
        if (result.getLeft()) {
            houseInfo.setHaInfo(haSimpleInfo);
            Map<String, List<Dict>> dictMap = dictService.getDictMap(null);
            EvaluationRecord record = evaluationService.saveEvaluationResult(result.getRight(), houseInfo, dictMap, evalType);
            return ResultMessage.success(record);
        } else {
            return ResultMessage.apiError(result.getRight());
        }
    }

    /**
     * 重新估价接口
     *
     * @param evalCode 估价唯一码
     * @param evalType         估价方法
     * @return 估价结果
     */
    @ApiOperation(value = "重新估价接口")
    @PostMapping("/reval")
    public ResultMessage reval(@ApiParam(value = "估价唯一码", required = true)
                               @RequestParam String evalCode,
                               @ApiParam(value = "估价类型，1：售价，2：租金")
                               @Pattern(regexp = "[1,2]", message = "evalType参数错误")
                               @RequestParam(defaultValue = "1") String evalType) {

        EvalQuery query = new EvalQuery();
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();
        EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
        if (Objects.isNull(record)) {
            EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
            if (Objects.isNull(history)) {
                return ResultMessage.paramError();
            }
            query.setEvalCode(null);
            query.setEvalRecordId(history.getEvalId());
            record = evaluationService.getEvaluationRecord(query);
        }
        //重新估价
        HouseInfo houseInfo = EvaluationUtil.toHouseInfo(record);
        Pair<Boolean, String> result = transferApiService.rapid(houseInfo, evalType);
        if (result.getLeft()) {
            //估价成功更新估价信息，将上次结果写入历史表
            record = evaluationService.saveRevaluationInfo(record, result.getRight(), evalType);
            return ResultMessage.success(record);
        } else {
            return ResultMessage.apiError(result.getRight());
        }
    }

    /**
     * 生成询价报告
     *
     * @param evalCode 估价唯一码
     * @return 询价报告
     */
    @ApiOperation(value = "生成询价报告")
    @PostMapping("/inquiry/add")
    public ResultMessage inquiryReport(@ApiParam(value = "估价唯一码", required = true)
                                           @NotBlank(message = "参数不能为空")
                                           @RequestParam String evalCode) {

        EvalQuery query = new EvalQuery();
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();
        EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
        if (Objects.isNull(record)) {
            EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
            if (Objects.isNull(history)) {
                return ResultMessage.paramError();
            }
            if (Objects.nonNull(history.getInquiry()) && history.getInquiry() == 1) {
                return ResultMessage.requestError("询价报告已生成！");
            }
            //保存报告信息
            EvaluationReport report = evaluationService.saveHistoryInquiryReport(history);
            return ResultMessage.success(report);
        } else {
            if (Objects.nonNull(record.getInquiry()) && record.getInquiry() == 1) {
                return ResultMessage.requestError("询价报告已生成！");
            }
            //保存报告信息
            EvaluationReport report = evaluationService.saveInquiryReport(record);
            return ResultMessage.success(report);
        }
    }

    /**
     * 生成咨询报告
     *
     * @param evalCode 估价唯一码
     * @return 咨询报告
     */
    @ApiOperation(value = "生成咨询报告")
    @PostMapping("/advisory/add")
    public ResultMessage advisoryReport(@ApiParam(value = "估价唯一码", required = true)
                                            @NotBlank(message = "参数不能为空")
                                            @RequestParam String evalCode) {

        EvalQuery query = new EvalQuery();
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();
        EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
        if (Objects.isNull(record)) {
            EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
            if (Objects.isNull(history)) {
                return ResultMessage.paramError();
            }
            if (Objects.nonNull(history.getAdvisory()) && history.getAdvisory() == 1) {
                return ResultMessage.requestError("询价报告已生成！");
            }
            //保存报告信息
            EvaluationReport report = evaluationService.saveHistoryAdvisoryReport(history);
            return ResultMessage.success(report);
        } else {
            if (Objects.nonNull(record.getAdvisory()) && record.getAdvisory() == 1) {
                return ResultMessage.requestError("询价报告已生成！");
            }
            //保存报告信息
            EvaluationReport report = evaluationService.saveAdvisoryReport(record);
            return ResultMessage.success(report);
        }
    }

    /**
     * 查询询价报告
     *
     * @param evalCode 估价唯一码
     * @return 询价报告信息
     */
    @ApiOperation(value = "获取询价报告信息")
    @GetMapping("/inquiry/get")
    public ResultMessage queryInquiry(@ApiParam(value = "估价唯一码", required = true)
                                          @NotBlank(message = "evalCode 不能为空")
                                          @RequestParam String evalCode) {
        EvalQuery query = new EvalQuery();
        query.setType(EvalConstant.INQUIRY_REPORT);
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();

        Map<String, Object> map = Maps.newHashMap();
        putRecordOrHistory(query, map);
        if (MapUtils.isEmpty(map)) {
            return ResultMessage.paramError();
        }
        return ResultMessage.success(map);
    }

    /**
     * 查询咨询报告
     *
     * @param evalCode 估价唯一码
     * @return 咨询报告信息
     */
    @ApiOperation(value = "获取咨询报告信息")
    @GetMapping("/advisory/get")
    public ResultMessage queryAdvisory(@ApiParam(value = "估价唯一码", required = true)
                                           @NotBlank(message = "evalCode 不能为空")
                                       @RequestParam String evalCode) {
        EvalQuery query = new EvalQuery();
        query.setType(EvalConstant.ADVISORY_REPORT);
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();

        Map<String, Object> map = Maps.newHashMap();
        putRecordOrHistory(query, map);
        if (MapUtils.isEmpty(map)) {
            return ResultMessage.paramError();
        }
        return ResultMessage.success(map);
    }

    /**
     * 导出询价报告
     * @param evalCode 估价唯一码
     * @return 询价报告文件
     */
    @ApiOperation(value = "导出询价报告信息")
    @GetMapping("/inquiry/export")
    public ResponseEntity exportInquiryReport(@ApiParam(value = "估价唯一码", required = true)
                                                  @NotBlank(message = "evalCode 不能为空")
                                              @RequestParam String evalCode){
        EvalQuery query = new EvalQuery();
        query.setType(EvalConstant.INQUIRY_REPORT);
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();
        EvaluationReport report = evaluationService.getEvaluationReport(query);
        if(Objects.isNull(report)){
            return ResponseEntity.noContent().build();
        }
        if(StringUtils.isNotBlank(report.getReportPath())){
            String reportPath = avsConfig.getPath().getBase() + avsConfig.getPath().getReport() + report.getReportPath();
            return ExportUtil.getExportFileResponse(new File(reportPath), ReportConstant.INQUIRY_FILE_NAME, false);
        }else{
            Map<String, Object> map = getTemplateParam(evalCode, report);
            if (MapUtils.isEmpty(map)) {
                return ResponseEntity.noContent().build();
            }
            map.put("report", report);
            //创建并写入文件
            String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp() + FileUtil.genRelativePathByExt("html");
            File tmpFile = FileUtil.createDir(tmpPath);
            boolean success = FreeMarkerUtil.generateInquiryFile(map, tmpPath);
            log.info("[evaluation-report] inquiry report generate html path: {}, state: {}", tmpPath, success);
            if(success){
                String relativePath = FileUtil.genRelativePathByExt("pdf");
                String reportPath = avsConfig.getPath().getBase() + avsConfig.getPath().getReport() + relativePath;
                File reportFile = FileUtil.createDir(reportPath);
                success = PdfUtil.generatePdf(tmpPath, reportPath, 260, 1200);
                log.info("[evaluation-report] inquiry report generate pdf path: {}, state: {}", relativePath, success);
                if(success){
                    //保存报告路径
                    report.setReportPath(relativePath);
                    CommonUtil.doEvalCreateUpdateInfo(report, CreateUpdateEnum.UPDATE);
                    evaluationService.updateReportPath(report);
                    //删除临时文件
                    FileUtil.deleteFile(tmpFile);
                    return ExportUtil.getExportFileResponse(reportFile, ReportConstant.INQUIRY_FILE_NAME, false);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("生成咨询价告文件失败。");
    }

    /**
     * 导出咨询报告
     * @param evalCode 估价唯一码
     * @return 咨询报告文件
     */
    @ApiOperation(value = "导出咨询报告信息")
    @GetMapping("/advisory/export")
    public ResponseEntity exportAdvisoryReport(@ApiParam(value = "估价唯一码", required = true)
                                                   @NotBlank(message = "evalCode 不能为空")
                                               @RequestParam String evalCode) {
        EvalQuery query = new EvalQuery();
        query.setType(EvalConstant.ADVISORY_REPORT);
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();
        EvaluationReport report = evaluationService.getEvaluationReport(query);
        if(Objects.isNull(report)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到咨询报告记录");
        }
        if(StringUtils.isNotBlank(report.getReportPath())){
            String reportPath = avsConfig.getPath().getBase() + avsConfig.getPath().getReport() + report.getReportPath();
            return ExportUtil.getExportFileResponse(new File(reportPath), ReportConstant.ADVISORY_FILE_NAME, false);
        }else{
            //模板参数
            Map<String, Object> map = Maps.newHashMap();
            String haCode, cityCode, propType;
            Integer evalType;
            EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
            if (Objects.isNull(record)) {
                EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
                if (Objects.isNull(history)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到估价记录");
                }
                haCode = history.getHaCode();
                cityCode = history.getCityCode();
                propType = history.getPropType();
                evalType = history.getEvalType();
                map.put("record", history);
            } else {
                haCode = record.getHaCode();
                cityCode = record.getCityCode();
                propType = record.getPropType();
                evalType = record.getEvalType();
                map.put("record", record);
            }
            String tradeType;
            if(EvalConstant.EVAL_TYPE_SELL.equals(evalType.toString())){
                tradeType = "forsale";
            }else{
                tradeType = "lease";
            }
            Pair<Boolean, String> haDetailResult = transferApiService.getHaDetail(haCode, cityCode);
            Pair<Boolean, String> analysisResult = transferApiService.getAnalysis(cityCode,
                    haCode, propType, tradeType, "5", "price");
            HaDetail haDetail = null;
            if(haDetailResult.getLeft()){
                haDetail = JsonUtil.fromJson(haDetailResult.getRight(), HaDetail.class);
            }
            if(analysisResult.getLeft()){
                map.put("analysis", analysisResult.getRight());
            }
            map.put("report", report);
            map.put("haDetail", haDetail);
            map.put("resourcePath", avsConfig.getPath().getBase() + avsConfig.getPath().getReport());
            map.put("basePath", avsConfig.getPath().getBase());
            map.put("reportConfig", sysReportConfigService.getById(report.getReportConfigId()));
            //创建并写入文件
            String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp() + FileUtil.genRelativePathByExt("html");
            File tmpFile = FileUtil.createDir(tmpPath);
            boolean success = FreeMarkerUtil.generateAdvisoryFile(map, tmpPath);
            log.info("[evaluation-report] advisory report generate html path: {}, state: {}", tmpPath, success);
            if(success){
                String relativePath = FileUtil.genRelativePathByExt("pdf");
                String reportPath = avsConfig.getPath().getBase() + avsConfig.getPath().getReport() + relativePath;
                File reportFile = FileUtil.createDir(reportPath);
                success = PdfUtil.generatePdf(tmpPath, reportPath);
                log.info("[evaluation-report] advisory report generate pdf path: {}, state: {}", relativePath, success);
                if(success){
                    //保存报告路径
                    report.setReportPath(relativePath);
                    CommonUtil.doEvalCreateUpdateInfo(report, CreateUpdateEnum.UPDATE);
                    evaluationService.updateReportPath(report);
                    //删除临时文件
                    FileUtil.deleteFile(tmpFile);
                    return ExportUtil.getExportFileResponse(reportFile, ReportConstant.ADVISORY_FILE_NAME, false);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("生成咨询报告文件失败。");
    }

    /**
     * 估价记录信息
     *
     * @param evalCode 估价唯一码
     * @return 估价记录
     */
    @ApiOperation(value = "估价记录/历史详情")
    @GetMapping("/get")
    public ResultMessage evaluationRecord(@ApiParam(value = "估价唯一码", required = true)
                                              @NotBlank(message = "evalCode 不能为空")
                                          @RequestParam String evalCode) {
        Map<String, Object> map = Maps.newHashMap();
        EvalQuery query = new EvalQuery();
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();
        EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
        if (Objects.isNull(record)) {
            EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
            map.put("record", history);
        } else {
            map.put("record", record);
        }
        if(Objects.nonNull(map.get("record"))){
            List<HouseResourceSample> houseResourceSamples = evaluationService
                    .getHouseResourceSampleList(evalCode);
            List<EvaluationBeta> evaluationBetaList = evaluationService
                    .getEvaluationBetaList(evalCode);
            map.put("samples", houseResourceSamples);
            map.put("betas", evaluationBetaList);
        }
        return ResultMessage.success(map);
    }

    /**
     * 分页查询估价记录列表
     *
     * @param query 查询条件
     * @return 记录列表
     */
    @ApiOperation(value = "查询估价记录列表")
    @GetMapping("/list")
    public ResultMessage list(@Valid @ModelAttribute EvalQuery query) {
        //根据当前用户查询信息
        query.authInfo();
        PageInfo<EvaluationRecord> pageInfo = evaluationService.getEvaluationRecordList(query);
        return ResultMessage.success(pageInfo);
    }

    /**
     * 导出估价记录列表
     *
     * @param query 查询条件
     * @return excel
     */
    @ApiOperation(value = "导出估价记录列表")
    @GetMapping("/export")
    public ResponseEntity exportList(@Valid @ModelAttribute EvalQuery query) {
        //查询列表
        //根据当前用户查询信息
        query.authInfo();
        List<EvaluationRecord> recordList = evaluationService.getAllEvaluationRecordList(query);
        if (CollectionUtils.isEmpty(recordList)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no results");
        }
        //转换导出对象
        List<EvalRecordExportInfo> exportInfoList = Lists.newArrayList();
        for (int i = 0; i < recordList.size(); i++) {
            EvaluationRecord record = recordList.get(i);
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
        //导出文件
        String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp();
        return ExportUtil.getExportExcelResponse(tmpPath, "估价记录.xlsx",
                EvalRecordExportInfo.class, exportInfoList);
    }

    /**
     * 查询估价历史记录列表
     *
     * @param evalRecordId 估价记录ID
     * @return 估价历史记录
     */
    @ApiOperation(value = "查询估价历史记录列表")
    @GetMapping("/history/list")
    public ResultMessage history(@ApiParam(value = "估价记录ID", required = true)
                                 @RequestParam Integer evalRecordId) {
        EvalQuery query = new EvalQuery();
        query.setEvalRecordId(evalRecordId);
        //根据当前用户查询信息
        query.authInfo();
        EvaluationRecord record = evaluationService.getEvaluationRecord(query);
        List<EvaluationRecordHistory> historyList = evaluationService.getEvaluationRecordHistoryList(query);
        List<Object> hisList = new ArrayList<>();
        hisList.add(record);
        if (CollectionUtils.isNotEmpty(historyList)) {
            hisList.addAll(historyList);
        }
        return ResultMessage.success(hisList);
    }

    /**
     * 获取询价报告模板参数
     *
     * @param evalCode 估价唯一码
     * @return 模板参数
     */
    private Map<String, Object> getTemplateParam(String evalCode, EvaluationReport report) {
        EvalQuery query = new EvalQuery();
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        query.authInfo();
        //模板参数
        Map<String, Object> map = Maps.newHashMap();
        String haCode, cityCode, propType;
        Integer evalType;
        Double gpsLon, gpsLat;
        EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
        if (Objects.isNull(record)) {
            EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
            if (Objects.isNull(history)) {
                return map;
            }
            haCode = history.getHaCode();
            cityCode = history.getCityCode();
            propType = history.getPropType();
            gpsLat = history.getGpsLat();
            gpsLon = history.getGpsLon();
            evalType = history.getEvalType();
            map.put("record", history);
        } else {
            haCode = record.getHaCode();
            cityCode = record.getCityCode();
            propType = record.getPropType();
            gpsLat = record.getGpsLat();
            gpsLon = record.getGpsLon();
            evalType = record.getEvalType();
            map.put("record", record);
        }
        String tradeType;
        if(EvalConstant.EVAL_TYPE_SELL.equals(evalType.toString())){
            tradeType = "forsale";
        }else{
            tradeType = "lease";
        }
        //房源信息
        List<HouseResourceSample> houseResourceSamples = evaluationService.getHouseResourceSampleList(evalCode);
        //雷达图信息
        List<EvaluationBeta> evaluationBetaList = evaluationService.getEvaluationBetaList(evalCode);
        //小区详情信息
        Pair<Boolean, String> haDetailResult = transferApiService.getHaDetail(haCode, cityCode);
        //行情信息-价格分布比较
        Pair<Boolean, String> distributionResult = transferApiService.getDistribution(cityCode,
                haCode, propType, tradeType, "price");
        //行情信息-房价行情走势
        Pair<Boolean, String> analysisResult = transferApiService.getAnalysis(cityCode,
                haCode, propType, tradeType, "2", "price");
        //行情信息-供给量
        Pair<Boolean, String> countResult = transferApiService.getAnalysis(cityCode,
                haCode, propType, tradeType, "2", "count");
        //附近楼盘价格对比
        Pair<Boolean, String> haList;
        if (Objects.nonNull(gpsLat)) {
            haList = transferApiService.getHaListByPoint(cityCode, propType, gpsLon, gpsLat, 1000, 20);
        } else {
            haList = transferApiService.getHaListByHaCode(cityCode, propType, haCode, 1000);
        }
        //公共交通
        Pair<Boolean, String> aroundList = transferApiService.getAround(cityCode, haCode);
        HaDetail haDetail = null;
        if (haDetailResult.getLeft()) {
            haDetail = JsonUtil.fromJson(haDetailResult.getRight(), HaDetail.class);
        }
        map.put("samples", houseResourceSamples);
        if (CollectionUtils.isNotEmpty(evaluationBetaList)) {
            map.put("betas", evaluationBetaList);
            map.put("betasJson", JsonUtil.toJson(evaluationBetaList));
        }
        map.put("haDetail", haDetail);
        if (distributionResult.getLeft()) {
            map.put("distribution", distributionResult.getRight());
        }
        if (analysisResult.getLeft()) {
            map.put("analysis", analysisResult.getRight());
        }
        if (countResult.getLeft()) {
            map.put("countList", countResult.getRight());
        }
        if (haList.getLeft()) {
            map.put("haList", haList.getRight());
        }
        if (aroundList.getLeft()) {
            map.put("aroundList", aroundList.getRight());
        }
        map.put("resourcePath", avsConfig.getPath().getBase() + avsConfig.getPath().getReport());
        map.put("reportConfig", sysReportConfigService.getById(report.getReportConfigId()));
        map.put("currentDate", DateUtils.addMonths(DateUtil.now(), -1));
        return map;
    }

    private void putRecordOrHistory(EvalQuery query, Map<String, Object> map) {
        EvaluationReport report = evaluationService.getEvaluationReport(query);
        if (Objects.nonNull(report)) {
            map.put("report", report);
            EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
            if (Objects.isNull(record)) {
                EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
                if(Objects.nonNull(history)){
                    map.put("record", history);
                    report.setUnitPrice(history.getUnitPrice());
                    report.setTotalPrice(history.getTotalPrice());
                }
            } else {
                map.put("record", record);
                report.setUnitPrice(record.getUnitPrice());
                report.setTotalPrice(record.getTotalPrice());
            }
        }
    }

}
