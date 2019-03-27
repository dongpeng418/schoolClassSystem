package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.constant.EvalConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.EvalQuery;
import cn.com.school.classinfo.dto.Beta;
import cn.com.school.classinfo.dto.House;
import cn.com.school.classinfo.dto.HouseInfo;
import cn.com.school.classinfo.dto.ResponseResult;
import cn.com.school.classinfo.dto.Sample;
import cn.com.school.classinfo.dto.Summary;
import cn.com.school.classinfo.mapper.EvaluationBetaMapper;
import cn.com.school.classinfo.mapper.EvaluationRecordHistoryMapper;
import cn.com.school.classinfo.mapper.EvaluationRecordMapper;
import cn.com.school.classinfo.mapper.EvaluationReportMapper;
import cn.com.school.classinfo.mapper.HouseResourceSampleMapper;
import cn.com.school.classinfo.mapper.SysReportConfigMapper;
import cn.com.school.classinfo.model.Dict;
import cn.com.school.classinfo.model.EvaluationBeta;
import cn.com.school.classinfo.model.EvaluationRecord;
import cn.com.school.classinfo.model.EvaluationRecordHistory;
import cn.com.school.classinfo.model.EvaluationReport;
import cn.com.school.classinfo.model.HouseResourceSample;
import cn.com.school.classinfo.model.SysReportConfig;
import cn.com.school.classinfo.service.EvaluationService;
import cn.com.school.classinfo.service.RiskCoefficientService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.EvaluationUtil;
import cn.com.school.classinfo.utils.JsonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 评估服务实现类
 *
 * @author dongpp
 * @date 2018/10/24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRecordMapper evaluationRecordMapper;

    private final HouseResourceSampleMapper houseResourceSampleMapper;

    private final EvaluationRecordHistoryMapper evaluationRecordHistoryMapper;

    private final EvaluationReportMapper evaluationReportMapper;

    private final EvaluationBetaMapper evaluationBetaMapper;

    private final RiskCoefficientService riskCoefficientService;

    private final SysReportConfigMapper sysReportConfigMapper;

    @Autowired
    public EvaluationServiceImpl(EvaluationRecordMapper evaluationRecordMapper,
                                 HouseResourceSampleMapper houseResourceSampleMapper,
                                 EvaluationRecordHistoryMapper evaluationRecordHistoryMapper,
                                 EvaluationReportMapper evaluationReportMapper,
                                 EvaluationBetaMapper evaluationBetaMapper,
                                 RiskCoefficientService riskCoefficientService,
                                 SysReportConfigMapper sysReportConfigMapper) {
        this.evaluationRecordMapper = evaluationRecordMapper;
        this.houseResourceSampleMapper = houseResourceSampleMapper;
        this.evaluationRecordHistoryMapper = evaluationRecordHistoryMapper;
        this.evaluationReportMapper = evaluationReportMapper;
        this.evaluationBetaMapper = evaluationBetaMapper;
        this.riskCoefficientService = riskCoefficientService;
        this.sysReportConfigMapper = sysReportConfigMapper;
    }

    /**
     * 保存估价结果信息
     *
     * @param json      中转接口返回的结果JSON
     * @param houseInfo 查询房屋信息
     * @param dictMap   字典信息
     * @return 估价记录信息
     */
    @Override
    public EvaluationRecord saveEvaluationResult(String json, HouseInfo houseInfo, Map<String,
            List<Dict>> dictMap, String evalType) {
        log.debug("save evaluation result, json: {}", json);
        ResponseResult responseResult = JsonUtil.fromJson(json, ResponseResult.class);
        EvaluationRecord record = toEvaluationRecord(responseResult, evalType);
        //使用前端的小区名称
        if (Objects.nonNull(houseInfo.getHaInfo())
                && StringUtils.isNotBlank(houseInfo.getHaInfo().getHaName())) {
            record.setHaName(houseInfo.getHaInfo().getHaName());
        }
        List<HouseResourceSample> houseResourceSamples = toHouseResourceSampleList(responseResult, record.getEvalCode());
        List<EvaluationBeta> evaluationBetaList = toEvaluationBetaList(responseResult, record.getEvalCode());
        log.debug("insert evaluation record, evalCode is {}", record.getEvalCode());
        //设置房屋信息
        setRecordDictInfo(record, houseInfo, dictMap);
        record.setEvalType(Integer.valueOf(evalType));
        //插入评估记录
        CommonUtil.doEvalCreateUpdateInfo(record, CreateUpdateEnum.CREATE);
        EvaluationUtil.riskFilter(record, riskCoefficientService.listAll());
        evaluationRecordMapper.insert(record);
        //插入房源信息
        if (CollectionUtils.isNotEmpty(houseResourceSamples)) {
            log.debug("insert houseResourceSample, size:{}", houseResourceSamples.size());
            houseResourceSampleMapper.batchInsert(houseResourceSamples);
        }
        //插入雷达图信息
        if (CollectionUtils.isNotEmpty(evaluationBetaList)) {
            log.debug("insert evaluationBeta, size:{}", evaluationBetaList.size());
            evaluationBetaMapper.batchInsert(evaluationBetaList);
        }
        return record;
    }

    /**
     * 根据ID查询估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    @Override
    public EvaluationRecord getEvaluationRecord(EvalQuery query) {
        return evaluationRecordMapper.selectByQuery(query);
    }

    /**
     * 保存重新估价信息
     *
     * @param record 估价记录
     * @param json   重估数据
     * @return 更新后的估价信息
     */
    @Override
    public EvaluationRecord saveRevaluationInfo(EvaluationRecord record, String json, String evalType) {
        log.debug("update revaluation result, json: {}", json);
        ResponseResult responseResult = JsonUtil.fromJson(json, ResponseResult.class);
        EvaluationRecord newRecord = toEvaluationRecord(responseResult, evalType);
        //已经重新生成evalCode
        List<HouseResourceSample> houseResourceSamples = toHouseResourceSampleList(responseResult, newRecord.getEvalCode());
        List<EvaluationBeta> evaluationBetaList = toEvaluationBetaList(responseResult, record.getEvalCode());
        //先插入估价历史
        saveEvaluationRecordHistory(record);
        //再更新估价记录的价格信息
        log.debug("update evaluation record, new evalCode is {}", newRecord.getEvalCode());
        newRecord.setId(record.getId());
        //增加估价历史数量
        int historyCount = Objects.isNull(record.getHistoryCount()) ? 1 : record.getHistoryCount() + 1;
        newRecord.setHistoryCount(historyCount);
        newRecord.setInquiry(0);
        newRecord.setAdvisory(0);
        newRecord.setEvalType(Integer.valueOf(evalType));
        newRecord.setBusinessTypeId(record.getBusinessTypeId());
        newRecord.setPrincipal(record.getPrincipal());
        newRecord.setOwner(record.getOwner());
        newRecord.setCardAddress(record.getCardAddress());
        CommonUtil.doEvalCreateUpdateInfo(newRecord, CreateUpdateEnum.CREATE);
        EvaluationUtil.riskFilter(newRecord, riskCoefficientService.listAll());
        evaluationRecordMapper.update(newRecord);
        //插入房源信息
        if (CollectionUtils.isNotEmpty(houseResourceSamples)) {
            log.debug("insert houseResourceSample, size:{}", houseResourceSamples.size());
            houseResourceSampleMapper.batchInsert(houseResourceSamples);
        }
        //插入雷达图信息
        if (CollectionUtils.isNotEmpty(evaluationBetaList)) {
            log.debug("insert evaluationBeta, size:{}", evaluationBetaList.size());
            evaluationBetaMapper.batchInsert(evaluationBetaList);
        }
        return newRecord;
    }

    /**
     * 根据估价记录唯一值和报告类型查询报告信息
     *
     * @param query 查询条件
     * @return 报告信息
     */
    @Override
    public EvaluationReport getEvaluationReport(EvalQuery query) {
        return evaluationReportMapper.selectByQuery(query);
    }

    /**
     * 更新估价报告文件路径
     *
     * @param report 估价报告
     */
    @Override
    public void updateReportPath(EvaluationReport report) {
        evaluationReportMapper.update(report);
    }

    /**
     * 保存询价报告信息
     *
     * @param record 估价记录
     * @return 报告信息
     */
    @Override
    public EvaluationReport saveInquiryReport(EvaluationRecord record) {
        return saveRecordReportInfo(record, EvalConstant.INQUIRY_REPORT);
    }

    /**
     * 保存咨询报告信息
     *
     * @param record 估价记录
     * @return 报告信息
     */
    @Override
    public EvaluationReport saveAdvisoryReport(EvaluationRecord record) {
        return saveRecordReportInfo(record, EvalConstant.ADVISORY_REPORT);
    }

    /**
     * 保存询价报告信息
     *
     * @param history 估价历史记录
     * @return 报告信息
     */
    @Override
    public EvaluationReport saveHistoryInquiryReport(EvaluationRecordHistory history) {
        return saveHistoryReportInfo(history, EvalConstant.INQUIRY_REPORT);
    }

    /**
     * 保存咨询报告信息
     *
     * @param history 估价历史记录
     * @return 报告信息
     */
    @Override
    public EvaluationReport saveHistoryAdvisoryReport(EvaluationRecordHistory history) {
        return saveHistoryReportInfo(history, EvalConstant.ADVISORY_REPORT);
    }

    /**
     * 分页查询估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    @Override
    public PageInfo<EvaluationRecord> getEvaluationRecordList(EvalQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        List<EvaluationRecord> recordList = evaluationRecordMapper.selectListByQuery(query);
        return new PageInfo<>(recordList);
    }

    /**
     * 根据条件查询所有估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    @Override
    public List<EvaluationRecord> getAllEvaluationRecordList(EvalQuery query) {
        return evaluationRecordMapper.selectListByQuery(query);
    }

    /**
     * 查询估价历史记录
     *
     * @param query 查询条件
     * @return 历史记录
     */
    @Override
    public EvaluationRecordHistory getEvaluationRecordHistory(EvalQuery query) {
        return evaluationRecordHistoryMapper.selectByQuery(query);
    }

    /**
     * 查询估价历史记录列表
     *
     * @param query 查询条件
     * @return 历史记录列表
     */
    @Override
    public List<EvaluationRecordHistory> getEvaluationRecordHistoryList(EvalQuery query) {
        return evaluationRecordHistoryMapper.selectListByQuery(query);
    }

    /**
     * 根据估价唯一码查询房源信息
     *
     * @param evalCode 估价唯一码
     * @return 房源信息列表
     */
    @Override
    public List<HouseResourceSample> getHouseResourceSampleList(String evalCode) {
        return houseResourceSampleMapper.selectByEvalCode(evalCode);
    }

    /**
     * 根据估价唯一码查询雷达图信息
     *
     * @param evalCode 估价唯一码
     * @return 雷达图信息列表
     */
    @Override
    public List<EvaluationBeta> getEvaluationBetaList(String evalCode) {
        return evaluationBetaMapper.select(evalCode);
    }

    /**
     * 根据evalCode查询估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    @Override
    public EvaluationRecord getRecordByEvalCode(EvalQuery query) {
        return evaluationRecordMapper.selectByQuery(query);
    }

    /**
     * 根据evalCode查询估价历史记录
     *
     * @param query 查询条件
     * @return 估价历史记录
     */
    @Override
    public EvaluationRecordHistory getRecordHistoryByEvalCode(EvalQuery query) {
        EvaluationRecordHistory history = evaluationRecordHistoryMapper.selectByQuery(query);
        if (Objects.isNull(history)) {
            return null;
        }
        return history;
    }

    /**
     * 更新估价记录资产管理状态
     *
     * @param record 估价记录
     */
    @Override
    public void updateAsset(EvaluationRecord record) {
        evaluationRecordMapper.update(record);
    }

    /**
     * 根据正式估价结果保存报告信息
     *
     * @param record     估价记录
     * @param reportType 报告类型
     */
    private EvaluationReport saveRecordReportInfo(EvaluationRecord record, String reportType) {

        // 获取报告配置信息
        SysReportConfig reportConfig = getSysReportConfig(reportType);
        // 保存报告信息
        EvaluationReport report = new EvaluationReport();
        report.setEvalDate(DateUtil.now());
        report.setUnitPrice(record.getUnitPrice());
        report.setTotalPrice(record.getTotalPrice());
        report.setReportType(reportType);
        report.setEvalCode(record.getEvalCode());
        report.setReportCode(EvaluationUtil.generateReportCode(record.getCityCode()));
        if(Objects.nonNull(reportConfig)){
            report.setReportConfigId(reportConfig.getId());
        }
        CommonUtil.doEvalCreateUpdateInfo(report, CreateUpdateEnum.CREATE);
        evaluationReportMapper.insert(report);

        //更新估价记录的报告信息
        if (EvalConstant.INQUIRY_REPORT.equals(reportType)) {
            record.setInquiry(1);
        } else {
            record.setAdvisory(1);
        }
        CommonUtil.doEvalCreateUpdateInfo(record, CreateUpdateEnum.UPDATE);
        evaluationRecordMapper.update(record);
        return report;
    }

    /**
     * 根据正式估价结果保存历史估价记录报告信息
     *
     * @param history    估价记录
     * @param reportType 报告类型
     */
    private EvaluationReport saveHistoryReportInfo(EvaluationRecordHistory history, String reportType) {
        // 获取报告配置信息
        SysReportConfig reportConfig = getSysReportConfig(reportType);
        // 保存报告信息
        EvaluationReport report = new EvaluationReport();
        report.setEvalDate(DateUtil.now());
        report.setUnitPrice(history.getUnitPrice());
        report.setTotalPrice(history.getTotalPrice());
        report.setReportType(reportType);
        report.setEvalCode(history.getEvalCode());
        report.setReportCode(EvaluationUtil.generateReportCode(history.getCityCode()));
        if(Objects.nonNull(reportConfig)){
            report.setReportConfigId(reportConfig.getId());
        }
        CommonUtil.doEvalCreateUpdateInfo(report, CreateUpdateEnum.CREATE);
        evaluationReportMapper.insert(report);

        //更新估价记录的报告信息
        if (EvalConstant.ADVISORY_REPORT.equals(reportType)) {
            history.setAdvisory(1);
        } else {
            history.setInquiry(1);
        }
        CommonUtil.doEvalCreateUpdateInfo(history, CreateUpdateEnum.UPDATE);
        evaluationRecordHistoryMapper.update(history);
        return report;
    }

    /**
     * 根据报告类型获取对应的报告配置信息
     *
     * @param reportType 报告类型
     * @return
     */
    private SysReportConfig getSysReportConfig(String reportType) {
        return sysReportConfigMapper.selectByReportType(Integer.valueOf(reportType), CommonUtil.getTenantId());
    }

    /**
     * 将估价结果转换为EvaluationRecord
     *
     * @param responseResult 估价结果
     * @return EvaluationRecord
     */
    private EvaluationRecord toEvaluationRecord(ResponseResult responseResult, String evalType) {
        EvaluationRecord record = new EvaluationRecord();
        HouseInfo houseInfo = responseResult.getTerm();
        BeanUtils.copyProperties(houseInfo, record);
        //小区信息
        if (Objects.nonNull(houseInfo.getHaInfo())) {
            record.setHaCode(houseInfo.getHaInfo().getHaCode());
            record.setHaName(houseInfo.getHaInfo().getHaName());
        }
        //GPS信息
        if (Objects.nonNull(houseInfo.getGpsInfo())) {
            String coord = houseInfo.getGpsInfo().getCoord();
            String[] coords = coord.split(",");
            Double lon = Double.valueOf(coords[0]);
            Double lat = Double.valueOf(coords[1]);
            if (StringUtils.isNotBlank(coord)) {
                record.setGpsLon(lon);
                record.setGpsLat(lat);
            }
            record.setGpsType(houseInfo.getGpsInfo().getCoordType());
        }
        //价格信息
        if (Objects.nonNull(responseResult.getAssessResult())) {
            Summary summary = responseResult.getAssessResult().getSummary();
            if (Objects.nonNull(summary)) {
                double amount;
                if (EvalConstant.EVAL_TYPE_SELL.equals(evalType)) {
                    amount = new BigDecimal(Double.toString(summary.getTotalPrice()))
                            .divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP).doubleValue();
                } else {
                    amount = summary.getTotalPrice();
                }
                record.setUnitPrice(summary.getUnitPrice());
                record.setTotalPrice(amount);
                record.setSamplePriceAvg(summary.getUnitPriceAVG());
                record.setSamplePriceMax(summary.getUnitPriceMax());
                record.setSamplePriceMin(summary.getUnitPriceMin());
                record.setSampleCount(summary.getCount());
                record.setEvalDate(DateUtil.now());
            }
        }
        //估价唯一码生成规则：城市码+小区码+当前时间+100到999之前随机数
        String evalCode = EvaluationUtil.generateEvalCode(record.getCityCode(), record.getHaCode());
        record.setEvalCode(evalCode);
        return record;
    }

    /**
     * 将估价结果转换为HouseResourceSample
     *
     * @param responseResult 估价结果
     * @return HouseResourceSampleMapper
     */
    private List<HouseResourceSample> toHouseResourceSampleList(ResponseResult responseResult, String evalCode) {
        if (Objects.isNull(responseResult)
                || Objects.isNull(responseResult.getAssessResult())
                || CollectionUtils.isEmpty(responseResult.getAssessResult().getSamples())) {
            log.warn("估价结果没有房源样例信息, evalCode: {}, result: {}", responseResult);
            return null;
        }
        List<Sample> samples = responseResult.getAssessResult().getSamples();
        List<HouseResourceSample> houseResourceSamples = Lists.newArrayList();
        samples.forEach(sample -> {
            HouseResourceSample houseResourceSample = new HouseResourceSample();
            House house = sample.getHouse();
            if (Objects.nonNull(house)) {
                BeanUtils.copyProperties(house, houseResourceSample);
                //区县
                if (Objects.nonNull(house.getDistInfo())) {
                    houseResourceSample.setDistCode(house.getDistInfo().getCode());
                    houseResourceSample.setDistName(house.getDistInfo().getName());
                }
                //小区
                if (Objects.nonNull(house.getHaInfo())) {
                    houseResourceSample.setHaCode(house.getHaInfo().getCode());
                    houseResourceSample.setHaName(house.getHaInfo().getName());
                }
                //街道
                if (Objects.nonNull(house.getStreetInfo())) {
                    houseResourceSample.setStreetCode(house.getStreetInfo().getCode());
                    houseResourceSample.setStreetName(house.getStreetInfo().getName());
                }
                //朝向
                if (Objects.nonNull(house.getFace())) {
                    houseResourceSample.setFaceCode(house.getFace().getCode());
                    houseResourceSample.setFaceName(house.getFace().getName());
                }
                //装修程度
                if (Objects.nonNull(house.getIntDeco())) {
                    houseResourceSample.setIntdecoCode(house.getIntDeco().getCode());
                    houseResourceSample.setIntdecoName(house.getIntDeco().getName());
                }
                //用途
                if (Objects.nonNull(house.getPropType())) {
                    houseResourceSample.setPropTypeCode(house.getPropType().getCode());
                    houseResourceSample.setPropTypeName(house.getPropType().getName());
                }
                houseResourceSample.setEvalCode(evalCode);
                houseResourceSample.setHrId(house.getId());
                houseResourceSample.setAreaSize(house.getBldgArea());
                houseResourceSample.setUnitPriceUnit(house.getUnitOfUnitPrice());
                houseResourceSample.setTotalPriceUnit(house.getUnitOfTotalPrice());

                CommonUtil.doEvalCreateUpdateInfo(houseResourceSample, CreateUpdateEnum.CREATE);
                houseResourceSamples.add(houseResourceSample);
            }
        });
        return houseResourceSamples;
    }

    private List<EvaluationBeta> toEvaluationBetaList(ResponseResult responseResult, String evalCode) {
        if (Objects.isNull(responseResult)
                || Objects.isNull(responseResult.getAssessResult())
                || CollectionUtils.isEmpty(responseResult.getAssessResult().getBetas())) {
            log.warn("估价结果没有雷达图信息,evalCode: {}, result: {} ", evalCode, responseResult);
            return null;
        }
        List<Beta> betaList = responseResult.getAssessResult().getBetas();
        List<EvaluationBeta> evaluationBetaList = Lists.newArrayList();
        betaList.forEach(beta -> {
            EvaluationBeta evaluationBeta = new EvaluationBeta();
            BeanUtils.copyProperties(beta, evaluationBeta);
            if (StringUtils.isBlank(beta.getTitle())) {
                if ("br".equalsIgnoreCase(beta.getName())) {
                    evaluationBeta.setTitle(EvalConstant.BR);
                } else if ("lr".equalsIgnoreCase(beta.getName())) {
                    evaluationBeta.setTitle(EvalConstant.LR);
                } else if ("bheight".equalsIgnoreCase(beta.getName())) {
                    evaluationBeta.setTitle(EvalConstant.B_HEIGHT);
                } else if ("floor".equalsIgnoreCase(beta.getName())) {
                    evaluationBeta.setTitle(EvalConstant.FLOOR);
                } else if ("bldgarea".equalsIgnoreCase(beta.getName())) {
                    evaluationBeta.setTitle(EvalConstant.BLDG_AREA);
                } else if ("face".equalsIgnoreCase(beta.getName())) {
                    evaluationBeta.setTitle(EvalConstant.FACE);
                } else if ("buildyear".equalsIgnoreCase(beta.getName())) {
                    evaluationBeta.setTitle(EvalConstant.BUILD_YEAR);
                } else if ("intdeco".equalsIgnoreCase(beta.getName())) {
                    evaluationBeta.setTitle(EvalConstant.INTDECO);
                }
            }
            evaluationBeta.setEvalCode(evalCode);

            CommonUtil.doEvalCreateUpdateInfo(evaluationBeta, CreateUpdateEnum.CREATE);
            evaluationBetaList.add(evaluationBeta);
        });
        return evaluationBetaList;
    }

    /**
     * 插入估价历史信息
     */
    private void saveEvaluationRecordHistory(EvaluationRecord record) {
        EvalQuery query = new EvalQuery();
        query.setEvalRecordId(record.getId());
        query.authInfo();
        record = evaluationRecordMapper.selectByQuery(query);
        EvaluationRecordHistory history = new EvaluationRecordHistory();
        BeanUtils.copyProperties(record, history);
        history.setId(null);
        history.setEvalId(record.getId());
        log.debug("save evaluation record history, evalCode is {}", history.getEvalCode());
        CommonUtil.doEvalCreateUpdateInfo(history, CreateUpdateEnum.CREATE);
        evaluationRecordHistoryMapper.insert(history);
    }

    /**
     * 设置字典信息
     *
     * @param record    估价记录
     * @param houseInfo 房屋信息
     */
    private void setRecordDictInfo(EvaluationRecord record, HouseInfo houseInfo, Map<String, List<Dict>> dictMap) {
        String faceType = "face";
        String propType = "propType";
        String bldgType = "bldgType";
        String intdecoType = "intDeco";
        String indoStruType = "indoStru";
        String proprtType = "proprt";
        record.setCityName(houseInfo.getCityName());
        record.setDistCode(houseInfo.getDistCode());
        record.setDistName(houseInfo.getDistName());
        record.setLocation(houseInfo.getLocation());
        record.setPrincipal(houseInfo.getPrincipal());
        record.setOwner(houseInfo.getOwner());
        record.setCardAddress(houseInfo.getCardAddress());
        record.setBusinessTypeId(houseInfo.getBusinessTypeId());
        //朝向
        if (!StringUtils.isAllBlank(houseInfo.getFace(), houseInfo.getFacename())) {
            if (StringUtils.isBlank(houseInfo.getFace())) {
                record.setFaceCode(getDictInfo(dictMap, houseInfo.getFacename(), faceType, true));
                record.setFaceName(houseInfo.getFacename());
            } else {
                record.setFaceName(getDictInfo(dictMap, houseInfo.getFace(), faceType, false));
                record.setFaceCode(houseInfo.getFace());
            }
        }
        //建筑类型
        if (!StringUtils.isAllBlank(houseInfo.getBldgType(), houseInfo.getBldgtypename())) {
            if (StringUtils.isBlank(houseInfo.getBldgType())) {
                record.setBldgTypeCode(getDictInfo(dictMap, houseInfo.getBldgtypename(), bldgType, true));
                record.setBldgTypeName(houseInfo.getBldgtypename());
            } else {
                record.setBldgTypeName(getDictInfo(dictMap, houseInfo.getBldgType(), bldgType, false));
                record.setBldgTypeCode(houseInfo.getBldgType());
            }
        }
        //装修程度
        if (!StringUtils.isAllBlank(houseInfo.getIntDeco(), houseInfo.getIntdeconame())) {
            if (StringUtils.isBlank(houseInfo.getIntDeco())) {
                record.setIntdecoCode(getDictInfo(dictMap, houseInfo.getIntdeconame(), intdecoType, true));
                record.setIntdecoName(houseInfo.getIntdeconame());
            } else {
                record.setIntdecoName(getDictInfo(dictMap, houseInfo.getIntDeco(), intdecoType, false));
                record.setIntdecoCode(houseInfo.getIntDeco());
            }
        }
        //室内结构
        if (!StringUtils.isAllBlank(houseInfo.getIndoStru(), houseInfo.getIndoStruName())) {
            if (StringUtils.isBlank(houseInfo.getIndoStru())) {
                record.setIndoStruCode(getDictInfo(dictMap, houseInfo.getIndoStruName(), indoStruType, true));
                record.setIndoStruName(houseInfo.getIndoStruName());
            } else {
                record.setIndoStruName(getDictInfo(dictMap, houseInfo.getIndoStru(), indoStruType, false));
                record.setIndoStruCode(houseInfo.getIndoStru());
            }
        }
        //房屋权属
        if (!StringUtils.isAllBlank(houseInfo.getProprt(), houseInfo.getProprtname())) {
            if (StringUtils.isBlank(houseInfo.getProprt())) {
                record.setProprtCode(getDictInfo(dictMap, houseInfo.getProprtname(), proprtType, true));
                record.setProprtName(houseInfo.getProprtname());
            } else {
                record.setProprtName(getDictInfo(dictMap, houseInfo.getProprt(), proprtType, false));
                record.setProprtCode(houseInfo.getProprt());
            }
        }
        //房屋用途
        if (!StringUtils.isBlank(houseInfo.getPropType())) {
            record.setPropTypeName(getDictInfo(dictMap, houseInfo.getPropType(), propType, false));
            record.setPropType(houseInfo.getPropType());
        }

    }

    /**
     * 获取字典的编码或名称
     *
     * @param key      编码或名称
     * @param dictType 字典类型编码
     * @param isCode   返回编码还是名称
     * @return 字典的编码或名称，如果isCode为true返回编码，false返回名称
     */
    private String getDictInfo(Map<String, List<Dict>> dictMap, String key, String dictType, boolean isCode) {
        List<Dict> dictList = dictMap.get(dictType);
        String value = null;
        for (Dict dict : dictList) {
            if (isCode) {
                if (dict.getDictName().equals(key)) {
                    value = dict.getDictCode();
                    break;
                }
            } else {
                if (dict.getDictCode().equals(key)) {
                    value = dict.getDictName();
                    break;
                }
            }
        }
        return value;
    }
}
