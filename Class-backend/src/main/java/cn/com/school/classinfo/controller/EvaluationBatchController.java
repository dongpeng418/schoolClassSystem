package cn.com.school.classinfo.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.Pair;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.BatchConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.BatchItemQuery;
import cn.com.school.classinfo.common.query.BatchQuery;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.dto.BatchItemStatistics;
import cn.com.school.classinfo.dto.HaSimpleInfo;
import cn.com.school.classinfo.dto.HouseInfo;
import cn.com.school.classinfo.dto.ResponseResult;
import cn.com.school.classinfo.dto.Summary;
import cn.com.school.classinfo.dto.excel.BatchItemExportInfo;
import cn.com.school.classinfo.dto.excel.EvalBatchUploadInfo;
import cn.com.school.classinfo.dto.filter.FilterHa;
import cn.com.school.classinfo.dto.filter.FilterItem;
import cn.com.school.classinfo.model.EvaluationBatch;
import cn.com.school.classinfo.model.EvaluationBatchItem;
import cn.com.school.classinfo.model.SysOrganization;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.queue.EvaluationBatchQueue;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.service.EvaluationBatchService;
import cn.com.school.classinfo.service.TransferApiService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.ExportUtil;
import cn.com.school.classinfo.utils.FileUtil;
import cn.com.school.classinfo.utils.JsonUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.com.school.classinfo.common.constant.BatchConstant.TEMPLATE_DOWNLOAD_NAME;
import static cn.com.school.classinfo.common.constant.BatchConstant.TEMPLATE_NAME;
import static cn.com.school.classinfo.common.constant.BatchConstant.UPLOAD_PATH;

/**
 * 批量估价相关接口
 *
 * @author dongpp
 * @date 2018/10/26
 */
@Api(tags = "批量估价相关接口", position = 5)
@Slf4j
@Validated
@RestController
@RequestMapping("/api/batch")
public class EvaluationBatchController {

    private final AvsConfig avsConfig;

    private final TransferApiService transferApiService;

    private final EvaluationBatchService evaluationBatchService;

    private final EvaluationBatchQueue batchQueue;

    private final AuthCommonService authCommonService;

    @Autowired
    public EvaluationBatchController(AvsConfig avsConfig,
                                     TransferApiService transferApiService,
                                     EvaluationBatchService evaluationBatchService,
                                     EvaluationBatchQueue batchQueue,
                                     AuthCommonService authCommonService) {
        this.avsConfig = avsConfig;
        this.transferApiService = transferApiService;
        this.evaluationBatchService = evaluationBatchService;
        this.batchQueue = batchQueue;
        this.authCommonService = authCommonService;
    }

    /**
     * 项目启动时将所有未完成的批量估价任务的批次号放入Redis队列
     */
    @PostConstruct
    public void init() {
        List<String> taskCodeList = evaluationBatchService.getTaskCodeList();
        if (CollectionUtils.isNotEmpty(taskCodeList)) {
            batchQueue.offerList(taskCodeList);
        }
    }

    /**
     * 模板文件下载
     *
     * @return 模板文件
     * @throws IOException 读文件异常
     */
    @ApiOperation(value = "批量估价模板下载")
    @GetMapping("/template")
    public ResponseEntity batchTemplate() throws IOException {
        String filePath = avsConfig.getPath().getBase() + avsConfig.getPath().getBatch() + TEMPLATE_NAME;
        File file = new File(filePath);
        if (!file.exists()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("file not found");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, FileUtil.getFileDownloadHeader(TEMPLATE_DOWNLOAD_NAME));
        headers.setContentLength(file.length());
        byte[] bytes;
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            log.error("read file error, file path: {}", filePath);
            throw new IOException(e);
        }
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);
    }

    /**
     * 批量估价上传接口
     *
     * @param file     上传的文件
     * @param taskName 任务名称
     * @param phone    接收手机号
     * @return 结果信息
     */
    @ApiOperation(value = "批量估价上传接口")
    @PostMapping("/upload")
    public ResultMessage upload(@ApiParam(value = "批量估价文件", required = true)
                                    @RequestParam MultipartFile file,
                                @ApiParam(value = "任务名称", required = true)
                                    @NotBlank(message = "taskName 不能为空")
                                    @RequestParam String taskName,
                                @ApiParam(value = "接通知收手机号")
                                    @RequestParam(required = false) String phone) {
        if (file.isEmpty()) {
            return ResultMessage.paramError("file");
        }

        //上传的文件保存到本地
        String uploadPath = UPLOAD_PATH + FileUtil.genRelativePath(file.getOriginalFilename());
        String filePath = avsConfig.getPath().getBase() + avsConfig.getPath().getBatch() + uploadPath;
        File localFile = FileUtil.createFile(filePath);
        if (Objects.isNull(localFile)) {
            log.error("create local file error, file path: {} ", filePath);
            return ResultMessage.requestError("保存上传文件失败");
        }
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            log.error("save upload file error, file path: {}, error: {}", filePath, e);
            return ResultMessage.requestError("保存上传文件失败");
        }

        //验证上传的文件
        List<EvalBatchUploadInfo> uploadInfos;
        try {
            uploadInfos = getBatchFileRecord(localFile);
        } catch (IOException e) {
            return ResultMessage.requestError(e.getMessage());
        }
        if (CollectionUtils.isEmpty(uploadInfos)) {
            return ResultMessage.paramError("上传文件没有记录");
        }

        //不能超过1W条
        if (uploadInfos.size() > 10000) {
            uploadInfos.clear();
            return ResultMessage.paramError("批量估价记录超过10000条");
        }

        //保存批量估价记录
        EvaluationBatch batch = new EvaluationBatch();
        batch.setTaskName(taskName);
        batch.setRecvPhone(phone);
        batch.setSubmitTime(DateUtil.now());
        batch.setEvalTotal(uploadInfos.size());
        //保存相对路径
        batch.setFilePath(uploadPath);
        batch.setOriginalFilename(file.getOriginalFilename());
        CommonUtil.doEvalCreateUpdateInfo(batch, CreateUpdateEnum.CREATE);

        //保存批量估价条目信息
        List<EvaluationBatchItem> itemList = uploadInfos.stream().map(uploadInfo -> {
            EvaluationBatchItem item = new EvaluationBatchItem();
            BeanUtils.copyProperties(uploadInfo, item);
            item.setEvalState(BatchConstant.ITEM_UNEVAL);
            CommonUtil.doEvalCreateUpdateInfo(item, CreateUpdateEnum.CREATE);
            return item;
        }).collect(Collectors.toList());
        uploadInfos.clear();
        evaluationBatchService.saveEvaluationBatch(batch, itemList);

        //将任务编码加入Redis队列
        batchQueue.offer(batch.getTaskCode());

        if (StringUtils.isNotBlank(phone)) {
            //发送批量估价开始通知短信
            sendStartNotify(batch);
        }
        return ResultMessage.success();
    }

    /**
     * 查询批量估价列表
     *
     * @param query 查询条件
     * @return 批量估价列表
     */
    @ApiOperation(value = "批量估价列表")
    @GetMapping("/list")
    public ResultMessage getBatchList(@Valid @ModelAttribute BatchQuery query) {
        //根据当前用户查询信息
        query.authInfo();
        PageInfo<EvaluationBatch> pageInfo = evaluationBatchService.getEvaluationBatchList(query);
        return ResultMessage.success(pageInfo);
    }

    /**
     * 查询批量估价信息
     *
     * @param id 查询条件
     * @return 批量估价
     */
    @ApiOperation(value = "批量估价信息")
    @GetMapping("/detail")
    public ResultMessage getBatchList(@ApiParam(value = "批量估价记录ID", required = true) @RequestParam Integer id) {
        BatchQuery query = new BatchQuery();
        query.setId(id);
        //根据当前用户查询信息
        SysUser user = CommonUtil.getLoginUser();
        query.setLoginUser(user.getLoginUser());
        EvaluationBatch batch = evaluationBatchService.getById(query);
        return ResultMessage.success(batch);
    }

    /**
     * 查询估价详细列表
     *
     * @param query 估价详细列表
     * @return 估价详细列表
     */
    @ApiOperation(value = "批量估价详细列表")
    @GetMapping("/items")
    public ResultMessage getBatchItemList(@Valid @ModelAttribute BatchItemQuery query) {
        if (Objects.isNull(query.getBatchId())) {
            return ResultMessage.paramError("batchId");
        }
        //根据当前用户查询信息
        query.authInfo();
        PageInfo<EvaluationBatchItem> pageInfo = evaluationBatchService.getBatchItemList(query);
        return ResultMessage.success(pageInfo);
    }

    /**
     * 导出批量估价详细列表
     *
     * @param batchId 批量估价ID
     * @param state   需要导出列表的状态
     * @return 文件
     */
    @ApiOperation(value = "导出批量估价详细列表")
    @GetMapping("/items/export")
    public ResponseEntity export(@ApiParam(value = "批量估价记录ID", required = true) @RequestParam Integer batchId,
                                 @ApiParam(value = "估价状态（0：未评估，1：评估成功，2：评估失败，3：解析失败）")
                                     @Range(min = 0, max = 3, message = "state 参数错误")
                                     @RequestParam(required = false) Integer state) {
        BatchItemQuery query = new BatchItemQuery();
        query.setBatchId(batchId);
        query.setEvalState(state);
        //根据当前用户查询信息
        query.authInfo();
        List<EvaluationBatchItem> itemList = evaluationBatchService.getUnPageBatchItemList(query);
        if (CollectionUtils.isEmpty(itemList)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no results");
        }
        //转换导出对象
        List<BatchItemExportInfo> exportInfoList = Lists.newArrayList();
        for (int i = 0; i < itemList.size(); i++) {
            EvaluationBatchItem record = itemList.get(i);
            BatchItemExportInfo exportInfo = new BatchItemExportInfo();
            BeanUtils.copyProperties(record, exportInfo);
            exportInfo.setOrderNo(i + 1);
            exportInfo.setTotalPrice(exportInfo.getTotalPrice());
            exportInfoList.add(exportInfo);
        }
        //导出Excel
        String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp();
        return ExportUtil.getExportExcelResponse(tmpPath, getBatchItemFileName(state),
                BatchItemExportInfo.class, exportInfoList);
    }

    /**
     * 定时从Redis队列中获取未完成的任务，然后去批量估价接口查询任务完成状态，
     * 并下载估价后的excel文件，解析里面的记录并保存到数据库中
     * 每5分钟调度一次，一次取20条记录进行处理
     */
    @Async
    @Scheduled(fixedDelay = 5*60*1000)
    public void handleUnfinishedTask() {
        for (int i = 0; i < 20; i++) {
            String taskCode = batchQueue.take();
            if (StringUtils.isBlank(taskCode)) {
                break;
            }
            this.handle(taskCode);
        }
    }

    /**
     * 根据任务编码来处理未完成的任务
     *
     * @param taskCode 任务编码
     */
    @Async
    public void handle(String taskCode) {
        EvaluationBatch batch = evaluationBatchService.getByTaskCode(taskCode);
        if (Objects.isNull(batch)) {
            return;
        }
        try {
            //分页查询批量估价的条目信息，每次查询100条
            BatchItemQuery query = new BatchItemQuery();
            query.setPageSize(100);
            query.setEvalState(BatchConstant.ITEM_UNEVAL);
            query.setBatchId(batch.getId());
            PageInfo<EvaluationBatchItem> pageInfo;
            do {
                //查询下一页
                pageInfo = evaluationBatchService.getBatchItemList(query);
                List<EvaluationBatchItem> itemList = pageInfo.getList();
                if (CollectionUtils.isEmpty(itemList)) {
                    break;
                }
                log.info("[evaluation-batch] search unfinished task, taskCode={}, current size={}", taskCode, itemList.size());
                Date now = DateUtil.now();
                for (EvaluationBatchItem item : itemList) {
                    //地址清洗
                    if (StringUtils.isAnyBlank(item.getHaName(), item.getCityName())) {
                        item.setEvalState(BatchConstant.ITEM_ERROR);
                        continue;
                    }
                    Pair<Boolean, String> result = transferApiService.addressMore(item.getHaName(),
                            item.getCityName(), item.getDistName(), 1);
                    //地址清洗成功
                    if (result.getLeft()) {
                        FilterItem filterResult = JsonUtil.fromJson(result.getRight(), FilterItem.class);
                        //清洗成功但是没有结果也算清洗失败
                        List<FilterHa> filterHaList = filterResult.getPosition();
                        if (CollectionUtils.isEmpty(filterHaList)) {
                            item.setEvalState(BatchConstant.ITEM_ERROR);
                            continue;
                        }
                        //获取第一个匹配结果，即使有多条
                        FilterHa filterHa = filterHaList.get(0);
                        copyBatchItem(item, filterHa);
                        //快速估价
                        Pair<Boolean, String> rapid = transferApiService.rapid(toHouseInfo(item), "1");
                        //估价成功
                        if (rapid.getLeft()) {
                            ResponseResult responseResult = JsonUtil.fromJson(rapid.getRight(), ResponseResult.class);
                            Summary summary = responseResult.getAssessResult().getSummary();
                            double amount = new BigDecimal(Double.toString(summary.getTotalPrice()))
                                    .divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP).doubleValue();
                            item.setTotalPrice(amount);
                            item.setUnitPrice(summary.getUnitPrice());
                            item.setEvalState(BatchConstant.ITEM_SUCCESS);
                            item.setEvalDate(now);
                        }
                        //估价失败
                        else {
                            log.info("eval failed. msg: {}", rapid.getRight());
                            item.setEvalState(BatchConstant.ITEM_FAILED);
                        }
                    }
                    //地址清洗失败
                    else {
                        log.info("address cleaning failed. msg: {}", result.getRight());
                        item.setEvalState(BatchConstant.ITEM_ERROR);
                    }
                    item.setUpdateTime(now);

                    //调用接口后休眠300ms
                    Thread.sleep(300);
                }
                evaluationBatchService.updateItemList(itemList);
                itemList.clear();
            } while (pageInfo.isHasNextPage());

            //获取批量估价任务统计信息
            List<BatchItemStatistics> statisticsList = evaluationBatchService.getStatisticsByBatchId(batch.getId());
            statisticsList.forEach(batchItemStatistics -> {
                switch (batchItemStatistics.getEvalState()) {
                    case 1:
                        BigDecimal bigDecimal = new BigDecimal(Double.toString(batchItemStatistics.getTotalAmount()))
                                .divide(new BigDecimal(10000), 4, RoundingMode.DOWN);
                        batch.setEvalSuccess(batchItemStatistics.getCount());
                        batch.setEvalAmount(bigDecimal.doubleValue());
                        break;
                    case 2:
                        batch.setEvalFail(batchItemStatistics.getCount());
                        break;
                    case 3:
                        batch.setEvalError(batchItemStatistics.getCount());
                        break;
                    default:
                }

            });
            //更新批量评估记录
            Date now = DateUtil.now();
            batch.setFinishTime(now);
            batch.setTaskState(BatchConstant.TASK_FINISH);
            batch.setUpdateTime(now);
            evaluationBatchService.updateState(batch);

            //发送批量评估完成通知短信
            if (StringUtils.isNotBlank(batch.getRecvPhone())) {
                String totalAmountStr = (Objects.isNull(batch.getEvalAmount()) ? 0 : batch.getEvalAmount()) + "亿元";
                sendEndNotify(batch, totalAmountStr);
            }
        } catch (Exception e) {
            log.error("handler batch eval task error.", e);
            batchQueue.offer(taskCode);
        }
    }

    /**
     * 发送批量估价开始通知短信
     *
     * @param batch 批量估价记录
     */
    private void sendStartNotify(EvaluationBatch batch) {
        Integer orgId = CommonUtil.getLoginUser().getOrganizationId();
        SysOrganization organization = authCommonService.findOrgInfoById(orgId);
        Map<String, String> param = Maps.newHashMap();
        String orgName = "客户";
        if(Objects.nonNull(organization)){
            orgName = organization.getName();
        }
        param.put("name", orgName);
        param.put("addtime", DateFormatUtils.format(batch.getSubmitTime(), "yyyy-MM-dd HH:mm:ss"));
        param.put("taskname", batch.getTaskName());
        Pair<Boolean, String> result = transferApiService.sendSms(batch.getRecvPhone(),
                avsConfig.getApi().getBatchStartNotify(), param);
        log.info("send sms start notify, phone: {}, state: {}, message: {}", batch.getRecvPhone(),
                result.getLeft(), result.getRight());
    }

    /**
     * 发送批量估价结束通知短信
     *
     * @param batch 批量估价记录
     */
    private void sendEndNotify(EvaluationBatch batch, String totalAmount) {
        SysOrganization organization = authCommonService.findOrgInfoById(batch.getOrganizationId());
        Map<String, String> param = Maps.newHashMap();
        String orgName = "客户";
        if(Objects.nonNull(organization)){
            orgName = organization.getName();
        }
        param.put("name", orgName);
        param.put("addtime", DateFormatUtils.format(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"));
        param.put("taskname", batch.getTaskName());
        param.put("valueresult", totalAmount);
        Pair<Boolean, String> result = transferApiService.sendSms(batch.getRecvPhone(),
                avsConfig.getApi().getBatchFinishNotify(), param);
        log.info("send sms end notify, phone: {}, state: {}, message: {}", batch.getRecvPhone(),
                result.getLeft(), result.getRight());
    }

    /**
     * 获取上传文件的记录
     *
     * @param batchFile 批量估价文件
     * @return 记录列表
     */
    private List<EvalBatchUploadInfo> getBatchFileRecord(File batchFile) throws IOException {
        String ext = FilenameUtils.getExtension(batchFile.getName());
        List<EvalBatchUploadInfo> uploadInfos;
        if ("CSV".equalsIgnoreCase(ext)) {
            //如果以utf-8编码读取不到记录，则以GBK编码再读一次
            uploadInfos = getBatchFileRecordFromCsv(batchFile, null);
            if (CollectionUtils.isEmpty(uploadInfos)) {
                uploadInfos = getBatchFileRecordFromCsv(batchFile, "gbk");
            }
        } else {
            uploadInfos = getBatchFileRecordFromExcel(batchFile);
        }
        return uploadInfos;
    }

    /**
     * 从Excel中获取上传文件的记录
     *
     * @param batchFile 批量估价文件
     * @return 记录列表
     */
    private List<EvalBatchUploadInfo> getBatchFileRecordFromExcel(File batchFile) {
        ImportParams params = new ImportParams();
        return ExcelImportUtil.importExcel(
                batchFile,
                EvalBatchUploadInfo.class,
                params);
    }

    /**
     * 从Csv中获取上传文件的记录
     *
     * @param batchFile 批量估价文件
     * @return 记录列表
     */
    private List<EvalBatchUploadInfo> getBatchFileRecordFromCsv(File batchFile, String encode) throws IOException {
        List<EvalBatchUploadInfo> uploadInfos = Lists.newArrayList();
        encode = StringUtils.isBlank(encode) ? "utf-8" : encode;
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(batchFile), encode))
        ) {
            String line;
            EvalBatchUploadInfo uploadInfo;
            int index = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (index == 0 && !line.contains("小区名称")) {
                    return uploadInfos;
                }
                index++;
                if (line.contains("小区名称")) {
                    continue;
                }
                String[] arrays = line.split(",");
                if (arrays.length != 19) {
                    throw new IOException("file format error.");
                }
                uploadInfo = new EvalBatchUploadInfo();
                uploadInfo.setCityName(arrays[0]);
                uploadInfo.setDistName(arrays[1]);
                uploadInfo.setLocation(arrays[2]);
                uploadInfo.setHaName(arrays[3]);
                uploadInfo.setPropType(arrays[4]);
                uploadInfo.setBuildNo(arrays[5]);
                uploadInfo.setUnit(arrays[6]);
                uploadInfo.setRoomNo(arrays[7]);
                uploadInfo.setBldgTypeName(arrays[8]);
                uploadInfo.setBldgArea(NumberUtils.isCreatable(arrays[9]) ? Double.valueOf(arrays[9]) : null);
                uploadInfo.setBr(NumberUtils.isDigits(arrays[10]) ? Integer.valueOf(arrays[10]) : null);
                uploadInfo.setLr(NumberUtils.isDigits(arrays[11]) ? Integer.valueOf(arrays[11]) : null);
                uploadInfo.setCr(NumberUtils.isDigits(arrays[12]) ? Integer.valueOf(arrays[12]) : null);
                uploadInfo.setIndoStruName(arrays[13]);
                uploadInfo.setFloor(NumberUtils.isDigits(arrays[14]) ? Integer.valueOf(arrays[14]) : null);
                uploadInfo.setBheight(NumberUtils.isDigits(arrays[15]) ? Integer.valueOf(arrays[15]) : null);
                uploadInfo.setFaceName(arrays[16]);
                uploadInfo.setIntdecoName(arrays[17]);
                uploadInfo.setBuildYear(arrays[18]);
                uploadInfos.add(uploadInfo);
            }
        } catch (IOException e) {
            log.error("[evaluationBatch] read upload file error, filePath:{}, error:{}", batchFile.getAbsolutePath(), e);
            throw new IOException(e);
        }
        return uploadInfos;
    }

    /**
     * 从接口返回的数据更新Item数据
     *
     * @param item bathcItem
     * @param ha   地址清洗返回数据
     */
    private void copyBatchItem(EvaluationBatchItem item, FilterHa ha) {
        item.setCityCode(ha.getCityCode());
        item.setCityName(ha.getCityName());
        item.setDistCode(ha.getDistCode());
        item.setDistName(ha.getDistName());
        item.setGpsLon(ha.getLon());
        item.setGpsLat(ha.getLat());
        item.setHaCode(ha.getHaCode());
    }

    /**
     * 将估价记录信息转换为HouseInfo，用于估价接口查询
     *
     * @param item 估价记录信息
     * @return HouseInfo
     */
    private HouseInfo toHouseInfo(EvaluationBatchItem item) {
        HouseInfo houseInfo = new HouseInfo();
        BeanUtils.copyProperties(item, houseInfo);
        houseInfo.setFace(item.getFaceCode());
        houseInfo.setBldgType(item.getBldgTypeCode());
        houseInfo.setBldgtypename(item.getBldgTypeName());
        houseInfo.setIntDeco(item.getIntdecoCode());
        houseInfo.setIntdeconame(item.getIntdecoName());
        houseInfo.setIndoStru(item.getIndoStruCode());
        houseInfo.setIndoStruName(item.getIndoStruName());
        houseInfo.setProprt(item.getProprtCode());
        houseInfo.setProprtname(item.getProprtName());
        //小区信息
        if (StringUtils.isNotBlank(item.getHaCode())
                || StringUtils.isNotBlank(item.getHaName())) {
            HaSimpleInfo haInfo = new HaSimpleInfo();
            haInfo.setHaCode(item.getHaCode());
            haInfo.setHaName(item.getHaName());
            houseInfo.setHaInfo(haInfo);
        }
        return houseInfo;
    }

    /**
     * 获取批量评估记录导出文件名
     *
     * @param state 需要导出的记录状态
     * @return 文件名称
     */
    private String getBatchItemFileName(Integer state) {
        String fileName;
        if (Objects.isNull(state)) {
            state = -1;
        }
        switch (state) {
            case 1:
                fileName = "评估成功记录列表";
                break;
            case 2:
                fileName = "评估失败记录列表";
                break;
            case 3:
                fileName = "解析失败记录列表";
                break;
            default:
                fileName = "全部记录列表";
        }
        fileName += ".xlsx";
        return fileName;
    }
}
