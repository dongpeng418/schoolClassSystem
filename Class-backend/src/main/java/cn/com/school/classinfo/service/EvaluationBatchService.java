package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.BatchItemQuery;
import cn.com.school.classinfo.common.query.BatchQuery;
import cn.com.school.classinfo.dto.BatchItemStatistics;
import cn.com.school.classinfo.model.EvaluationBatch;
import cn.com.school.classinfo.model.EvaluationBatchItem;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 批量估价Service
 * @author dongpp
 * @date 2018/10/26
 */
public interface EvaluationBatchService {

    /**
     * 保存批量估价信息
     * @param batch 批量估价信息
     * @return 批量估价信息
     */
    EvaluationBatch saveEvaluationBatch(EvaluationBatch batch, List<EvaluationBatchItem> itemList);

    /**
     * 根据条件查询批量估价列表
     * @param query 查询条件
     * @return 估价列表
     */
    PageInfo<EvaluationBatch> getEvaluationBatchList(BatchQuery query);

    /**
     * 查询所有未完成任务的批次号
     * @return 任务号列表
     */
    List<String> getTaskCodeList();

    /**
     * 查询批量估价记录
     * @param taskCode 任务号
     * @return 批量估价记录
     */
    EvaluationBatch getByTaskCode(String taskCode);

    /**
     * 查询批量估价记录
     * @param query 查询条件
     * @return 批量估价记录
     */
    EvaluationBatch getById(BatchQuery query);

    /**
     * 更新任务状态
     * @param batch 批量估价信息
     */
    void updateState(EvaluationBatch batch);

    /**
     * 保存批量估价条目列表
     * @param batchItems 条目列表
     */
    void saveItemList(List<EvaluationBatchItem> batchItems);

    /**
     * 更新批量估价条目列表
     * @param batchItems 条目列表
     */
    void updateItemList(List<EvaluationBatchItem> batchItems);

    /**
     * 根据条件查询批量估价条目列表
     * @param query 查询条件
     * @return 条目列表
     */
    PageInfo<EvaluationBatchItem> getBatchItemList(BatchItemQuery query);

    /**
     * 根据条件查询批量估价条目列表（不分页，用于导出）
     * @param query 查询条件
     * @return 条目列表
     */
    List<EvaluationBatchItem> getUnPageBatchItemList(BatchItemQuery query);

    /**
     * 根据批量估价的ID来获取该批量估价的统计信息
     * @param batchId 批量估价ID
     * @return 统计信息
     */
    List<BatchItemStatistics> getStatisticsByBatchId(Integer batchId);

}
