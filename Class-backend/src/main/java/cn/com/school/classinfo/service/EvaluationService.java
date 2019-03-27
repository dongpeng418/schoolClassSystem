package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.EvalQuery;
import cn.com.school.classinfo.dto.HouseInfo;
import cn.com.school.classinfo.model.Dict;
import cn.com.school.classinfo.model.EvaluationBeta;
import cn.com.school.classinfo.model.EvaluationRecord;
import cn.com.school.classinfo.model.EvaluationRecordHistory;
import cn.com.school.classinfo.model.EvaluationReport;
import cn.com.school.classinfo.model.HouseResourceSample;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 评估Service
 *
 * @author dongpp
 * @date 2018/10/24
 */
public interface EvaluationService {

    /**
     * 根据ID查询估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    EvaluationRecord getEvaluationRecord(EvalQuery query);

    /**
     * 保存估价结果信息
     *
     * @param json      中转接口返回的结果JSON
     * @param houseInfo 估价房屋信息
     * @param dictMap   字典信息
     * @param evalType  估价类型
     * @return 估价记录信息
     */
    EvaluationRecord saveEvaluationResult(String json, HouseInfo houseInfo, Map<String, List<Dict>> dictMap, String evalType);

    /**
     * 保存重新估价信息
     *
     * @param evalRecordId 估价记录ID
     * @param json         重估数据
     * @param evalType     估价类型
     * @return 更新后的估价信息
     */
    EvaluationRecord saveRevaluationInfo(EvaluationRecord evalRecordId, String json, String evalType);

    /**
     * 根据估价记录唯一值和报告类型查询报告信息
     *
     * @param query 查询条件
     * @return 报告信息
     */
    EvaluationReport getEvaluationReport(EvalQuery query);

    /**
     * 更新估价报告文件路径
     *
     * @param report 估价报告
     */
    void updateReportPath(EvaluationReport report);

    /**
     * 保存询价报告信息
     *
     * @param record 估价记录
     * @return 报告信息
     */
    EvaluationReport saveInquiryReport(EvaluationRecord record);

    /**
     * 保存咨询报告信息
     *
     * @param record 估价记录
     * @return 报告信息
     */
    EvaluationReport saveAdvisoryReport(EvaluationRecord record);

    /**
     * 保存询价报告信息
     *
     * @param history 估价历史记录
     * @return 报告信息
     */
    EvaluationReport saveHistoryInquiryReport(EvaluationRecordHistory history);

    /**
     * 保存咨询报告信息
     *
     * @param history 估价历史记录
     * @return 报告信息
     */
    EvaluationReport saveHistoryAdvisoryReport(EvaluationRecordHistory history);

    /**
     * 分页查询估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    PageInfo<EvaluationRecord> getEvaluationRecordList(EvalQuery query);

    /**
     * 根据条件查询所有估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    List<EvaluationRecord> getAllEvaluationRecordList(EvalQuery query);

    /**
     * 查询估价历史记录
     *
     * @param query 查询条件
     * @return 历史记录
     */
    EvaluationRecordHistory getEvaluationRecordHistory(EvalQuery query);

    /**
     * 查询估价历史记录列表
     *
     * @param query 查询条件
     * @return 历史记录列表
     */
    List<EvaluationRecordHistory> getEvaluationRecordHistoryList(EvalQuery query);

    /**
     * 根据估价唯一码查询房源信息
     *
     * @param evalCode 估价唯一码
     * @return 房源信息列表
     */
    List<HouseResourceSample> getHouseResourceSampleList(String evalCode);

    /**
     * 根据估价唯一码查询雷达图信息
     *
     * @param evalCode 估价唯一码
     * @return 雷达图信息列表
     */
    List<EvaluationBeta> getEvaluationBetaList(String evalCode);

    /**
     * 根据evalCode查询估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    EvaluationRecord getRecordByEvalCode(EvalQuery query);

    /**
     * 根据evalCode查询估价历史记录
     *
     * @param query 查询条件
     * @return 估价历史记录
     */
    EvaluationRecordHistory getRecordHistoryByEvalCode(EvalQuery query);

    /**
     * 更新估价记录资产管理状态
     *
     * @param record 估价记录
     */
    void updateAsset(EvaluationRecord record);
}
