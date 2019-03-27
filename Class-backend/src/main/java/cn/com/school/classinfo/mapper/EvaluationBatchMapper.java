package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.BatchQuery;
import cn.com.school.classinfo.model.EvaluationBatch;

import java.util.List;

/**
 * 批量估价Mapper
 * @author dongpp
 * @date 2018/10/26
 */
public interface EvaluationBatchMapper {

    /**
     * 插入批量估价记录
     * @param batch 记录信息
     * @return 数量
     */
    int insert(EvaluationBatch batch);

    /**
     * 查询估价记录列表
     * @param query 查询条件
     * @return 列表
     */
    List<EvaluationBatch> selectListByQuery(BatchQuery query);

    /**
     * 查询批量估价记录
     * @param query 查询条件
     * @return 批量估价记录
     */
    EvaluationBatch selectByQuery(BatchQuery query);

    /**
     * 查询所有未完成任务的批次号
     * @return 批次号列表
     */
    List<String> selectAllTaskCode();

    /**
     * 更新任务状态
     * @param batch 批量估价信息
     * @return 数量
     */
    int update(EvaluationBatch batch);
}
