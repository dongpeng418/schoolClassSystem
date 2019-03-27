package cn.com.school.classinfo.utils;

import cn.com.school.classinfo.common.Pair;
import cn.com.school.classinfo.dto.GpsInfo;
import cn.com.school.classinfo.dto.HaSimpleInfo;
import cn.com.school.classinfo.dto.HouseInfo;
import cn.com.school.classinfo.model.EvaluationRecord;
import cn.com.school.classinfo.model.EvaluationRecordHistory;
import cn.com.school.classinfo.model.SysRiskCoefficient;
import cn.com.school.classinfo.vo.AssetVO;
import cn.com.school.classinfo.vo.RegionVO;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 估价工具类
 *
 * @author dongpp
 * @date 2018/10/24
 */
@Slf4j
public class EvaluationUtil {

    /**
     * 生成估价唯一编码
     *
     * @param params 关联参数
     * @return code
     */
    public static String generateEvalCode(String... params) {
        if (params.length == 0) {
            log.error("generate evalCode error, params is null");
            throw new IllegalArgumentException("generate evalCode error");
        }
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            if (StringUtils.isBlank(param)) {
                continue;
            }
            sb.append(param);
        }
        sb.append(System.currentTimeMillis());
        sb.append(String.format("%03d", RandomUtils.nextInt(0, 999)));
        return sb.toString();
    }

    /**
     * 生成任务编码
     *
     * @return 任务编码
     */
    public static String generateTaskCode() {
        return DateFormatUtils.format(DateUtil.now(), "yyyyMMddHHmmssSSS") + String.format("%03d", RandomUtils.nextInt(0, 999));
    }

    public static String generateReportCode(String cityCode) {
        return "Z"
                + DateFormatUtils.format(DateUtil.now(), "yyyyMMdd")
                + cityCode.toUpperCase()
                + String.format("%04d", RandomUtils.nextInt(0, 9999));
    }

    /**
     * 获取报告类型字符
     *
     * @param inquire  是否有询价报告
     * @param advisory 是否有评估报告
     * @return 报告类型
     */
    public static String getReportType(Integer inquire, Integer advisory) {
        boolean noInquiry = Objects.isNull(inquire) || inquire == 0;
        boolean noAdvisory = Objects.isNull(advisory) || advisory == 0;
        String report = null;
        if (noInquiry && noAdvisory) {
            report = "未生成";
        } else {
            if (!noInquiry) {
                report = "询价报告";
            }
            if (!noAdvisory) {
                if (noInquiry) {
                    report = "咨询报告";
                } else {
                    report += ",咨询报告";
                }
            }
        }
        return report;
    }

    /**
     * 将估价记录信息转换为HouseInfo，用于估价接口查询
     *
     * @param record 估价记录信息
     * @return HouseInfo
     */
    public static HouseInfo toHouseInfo(EvaluationRecord record) {
        HouseInfo houseInfo = new HouseInfo();
        BeanUtils.copyProperties(record, houseInfo);
        houseInfo.setFace(record.getFaceCode());
        houseInfo.setBldgType(record.getBldgTypeCode());
        houseInfo.setBldgtypename(record.getBldgTypeName());
        houseInfo.setIntDeco(record.getIntdecoCode());
        houseInfo.setIntdeconame(record.getIntdecoName());
        houseInfo.setIndoStru(record.getIndoStruCode());
        houseInfo.setIndoStruName(record.getIndoStruName());
        houseInfo.setProprt(record.getProprtCode());
        houseInfo.setProprtname(record.getProprtName());
        //小区信息
        if (StringUtils.isNotBlank(record.getHaCode())
                || StringUtils.isNotBlank(record.getHaName())) {
            HaSimpleInfo haInfo = new HaSimpleInfo();
            haInfo.setHaCode(record.getHaCode());
            haInfo.setHaName(record.getHaName());
            houseInfo.setHaInfo(haInfo);
        }
        //GPS信息
        if (Objects.nonNull(record.getGpsLat())) {
            GpsInfo gpsInfo = new GpsInfo();
            String coords = String.valueOf(record.getGpsLon()) + "," + String.valueOf(record.getGpsLat());
            gpsInfo.setCoord(coords);
            gpsInfo.setCoordType(record.getGpsType());
            houseInfo.setGpsInfo(gpsInfo);
        }
        return houseInfo;
    }

    /**
     * 将估价历史记录信息转换为HouseInfo，用于估价接口查询
     *
     * @param history 估价历史记录信息
     * @return HouseInfo
     */
    public static HouseInfo toHouseInfo(EvaluationRecordHistory history) {
        EvaluationRecord record = new EvaluationRecord();
        BeanUtils.copyProperties(history, record);
        return toHouseInfo(record);
    }

    /**
     * 根据风控系数来设置估价记录的总价和单价
     *
     * @param recordList      估价记录列表
     * @param coefficientList 风控系数
     */
    public static void riskFilter(List<EvaluationRecord> recordList, List<SysRiskCoefficient> coefficientList) {
        if (CollectionUtils.isEmpty(recordList) || CollectionUtils.isEmpty(coefficientList)) {
            return;
        }
        recordList.forEach(record -> riskFilter(record, coefficientList));
    }

    /**
     * 根据风控系数来设置估价记录的总价和单价
     *
     * @param record          估价记录
     * @param coefficientList 风控系数列表
     */
    public static void riskFilter(EvaluationRecord record, List<SysRiskCoefficient> coefficientList) {
        if (Objects.isNull(record) || CollectionUtils.isEmpty(coefficientList) || record.getEvalType() == 2) {
            return;
        }
        Pair<Double, Double> pair = riskFilter(record.getCityCode(), record.getDistCode(), record.getBusinessTypeId(),
                record.getTotalPrice(), record.getBldgArea(), coefficientList);
        if (Objects.nonNull(pair)) {
            record.setTotalPrice(pair.getLeft());
            record.setUnitPrice(pair.getRight());
        }
    }

    /**
     * 根据风控系数来设置估价记录的总价和单价
     *
     * @param cityCode        城市码
     * @param distCode        区县码
     * @param businessId      业务类型ID
     * @param bldgArea        面积
     * @param totalPrice      原总价
     * @param coefficientList 风控系数列表
     * @return 乘于风控系数后的总价和单价
     */
    public static Pair<Double, Double> riskFilter(String cityCode, String distCode, Integer businessId,
                                                  Double totalPrice, Double bldgArea,
                                                  List<SysRiskCoefficient> coefficientList) {
        String provinceKey = "PROVINCE";
        String cityKey = "CITY";
        String distKey = "DIST";
        String evalBusinessId = Objects.isNull(businessId) ? null : String.valueOf(businessId);
        SysRiskCoefficient result = null;
        Map<String, SysRiskCoefficient> resultMap = Maps.newHashMap();
        for (SysRiskCoefficient coefficient : coefficientList) {
            RegionVO regionVO = RegionUtil.getParent(cityCode);
            if (Objects.isNull(regionVO)) {
                continue;
            }
            /*
                1、[如果区县都为空] 或 [如果估价县区不为空，风险区县为空]
                    1、匹配城市
                        1、[如果估价城市不为空，风险城市为空]
                            1、匹配省
                                1、如果不同，跳过
                                2、如果相同
                                    1、匹配业务
                                        1、如果都为空，找到
                                        2、如果业务匹配成功，找到
                                        3、匹配不成功，跳过
                         2、[城市相同]
                            1、匹配业务
                3、如果都不为空
                    1、如果相同
                        同1匹配业务
                    2、如果不同跳过
            */
            String evalProvince = regionVO.getRegionCode();
            // [如果区县都为空] 或 [如果估价县区不为空，风险区县为空]
            boolean pass = StringUtils.isAllBlank(coefficient.getDistCode(), distCode)
                    || StringUtils.isBlank(coefficient.getDistCode());
            if (pass) {
                // [如果估价城市不为空，风险城市为空]
                if (StringUtils.isNotBlank(cityCode) && StringUtils.isBlank(coefficient.getCityCode())) {
                    //如果省匹配，则匹配业务ID
                    if (evalProvince.equals(coefficient.getProvinceCode())) {
                        //如果业务ID都为空，则直接匹配匹配，结束循环
                        if (StringUtils.isAllBlank(evalBusinessId, coefficient.getBusinessId())) {
                            resultMap.put(provinceKey, coefficient);
                        }
                        //否则验证业务ID是否匹配
                        else {
                            //如果都不为空
                            if (!StringUtils.isAnyBlank(evalBusinessId, coefficient.getBusinessId())) {
                                String[] businessIds = coefficient.getBusinessId().split(",");
                                if (StringUtils.equalsAny(evalBusinessId, businessIds)) {
                                    resultMap.put(provinceKey, coefficient);
                                }
                            }
                        }
                    }
                }
                //[城市相同]
                else if(cityCode.equals(coefficient.getCityCode())){
                    //如果业务ID都为空，则直接匹配匹配，结束循环
                    if (StringUtils.isAllBlank(evalBusinessId, coefficient.getBusinessId())) {
                        resultMap.put(cityKey, coefficient);
                    }
                    //否则验证业务ID是否匹配
                    else {
                        //如果都不为空
                        if (!StringUtils.isAnyBlank(evalBusinessId, coefficient.getBusinessId())) {
                            String[] businessIds = coefficient.getBusinessId().split(",");
                            if (StringUtils.equalsAny(evalBusinessId, businessIds)) {
                                resultMap.put(cityKey, coefficient);
                            }
                        }
                    }
                }
            } else {
                //如果区县不匹配，跳到下条
                if (StringUtils.isNotBlank(distCode) && distCode.equals(coefficient.getDistCode())) {
                    //如果业务ID都为空，则直接匹配匹配，结束循环
                    if (StringUtils.isAllBlank(evalBusinessId, coefficient.getBusinessId())) {
                        resultMap.put(distKey, coefficient);
                        break;
                    }
                    //否则验证业务ID是否匹配
                    else {
                        //如果都不为空
                        if (!StringUtils.isAnyBlank(evalBusinessId, coefficient.getBusinessId())) {
                            String[] businessIds = coefficient.getBusinessId().split(",");
                            if (StringUtils.equalsAny(evalBusinessId, businessIds)) {
                                resultMap.put(distKey, coefficient);
                                break;
                            }
                        }
                    }
                }
            }
        }
        //获取结果
        result = resultMap.get(distKey);
        if(Objects.isNull(result)){
            result = resultMap.get(cityKey);
            if(Objects.isNull(result)){
                result = resultMap.get(provinceKey);
            }
        }
        if (Objects.nonNull(result)) {
            double riskTotalPrice = new BigDecimal(totalPrice)
                    .multiply(new BigDecimal(result.getPriceCoefficient()))
                    .setScale(4, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            double riskUnitPrice;
            riskUnitPrice = new BigDecimal(riskTotalPrice)
                    .multiply(new BigDecimal(10000))
                    .divide(new BigDecimal(bldgArea), 4, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            return Pair.of(riskTotalPrice, riskUnitPrice);
        }

        return null;
    }

    /**
     * 根据风控系数来设置估价记录历史的总价和单价
     *
     * @param recordList      估价记录列表
     * @param coefficientList 风控系数
     */
    public static void riskFilterHistory(List<EvaluationRecordHistory> recordList, List<SysRiskCoefficient> coefficientList) {
        if (CollectionUtils.isEmpty(recordList) || CollectionUtils.isEmpty(coefficientList)) {
            return;
        }
        recordList.forEach(record -> riskFilterHistory(record, coefficientList));
    }

    /**
     * 根据风控系数来设置估价记录历史的总价和单价
     *
     * @param record          估价记录
     * @param coefficientList 风控系数列表
     */
    public static void riskFilterHistory(EvaluationRecordHistory record, List<SysRiskCoefficient> coefficientList) {
        if (record.getEvalType() == 2) {
            return;
        }
        Pair<Double, Double> pair = riskFilter(record.getCityCode(), record.getDistCode(), record.getBusinessTypeId(),
                record.getTotalPrice(), record.getBldgArea(), coefficientList);
        if (Objects.nonNull(pair)) {
            record.setTotalPrice(pair.getLeft());
            record.setUnitPrice(pair.getRight());
        }
    }

    /**
     * 根据风控系数来设置资产列表的总价和单价
     *
     * @param assetList       资产列表
     * @param coefficientList 风控系数列表
     */
    public static void riskFilterAsset(List<AssetVO> assetList, List<SysRiskCoefficient> coefficientList) {
        if (CollectionUtils.isEmpty(assetList) || CollectionUtils.isEmpty(coefficientList)) {
            return;
        }
        assetList.forEach(assetVO -> {
            if (assetVO.getEvalType() == 2) {
                return;
            }
            Pair<Double, Double> pair = riskFilter(assetVO.getCityCode(), assetVO.getDistCode(), assetVO.getBusinessTypeId(),
                    assetVO.getTotalPrice(), assetVO.getBldgArea(), coefficientList);
            if (Objects.nonNull(pair)) {
                assetVO.setTotalPrice(pair.getLeft());
                assetVO.setUnitPrice(pair.getRight());
            }
        });
    }

}
