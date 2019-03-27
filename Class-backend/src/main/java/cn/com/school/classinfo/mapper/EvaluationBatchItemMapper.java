package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.BatchItemQuery;
import cn.com.school.classinfo.dto.BatchItemStatistics;
import cn.com.school.classinfo.model.EvaluationBatchItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批量估价条目Mapper
 * @author dongpp
 * @date 2018/10/28
 */
public interface EvaluationBatchItemMapper {

    /**
     * 插入批量评估记录条目列表
     * @param batchItems 条目列表
     * @return 数量
     */
    int batchInsert(List<EvaluationBatchItem> batchItems);

    /**
     * 查询批量估价记录条目列表
     * @param query 条件
     * @return 条目列表
     */
    List<EvaluationBatchItem> selectListByQuery(BatchItemQuery query);

    /**
     * 根据批量估价的ID来获取该批量估价的统计信息
     * @param batchId 批量估价ID
     * @return 统计信息
     */
    List<BatchItemStatistics> selectStatisticsByBatchId(@Param("batchId") Integer batchId);

    /**
     * 批量更新条目信息
     * @param items 条目列表
     * @return 更新数量
     */
    int batchUpdate(List<EvaluationBatchItem> items);
}
