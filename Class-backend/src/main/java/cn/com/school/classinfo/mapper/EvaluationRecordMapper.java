package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.EvalQuery;
import cn.com.school.classinfo.model.EvaluationRecord;

import java.util.List;

/**
 * 评估记录Mapper
 *
 * @author dongpp
 * @date 2018/10/24
 */
public interface EvaluationRecordMapper {

    /**
     * 插入评估记录
     *
     * @param record 评估记录
     * @return 数量
     */
    int insert(EvaluationRecord record);

    /**
     * 根据ID查询估价记录
     *
     * @param query 查询条件
     * @return 估价记录
     */
    EvaluationRecord selectByQuery(EvalQuery query);

    /**
     * 更新估价记录的报告状态
     *
     * @param record 报告信息
     * @return 数量
     */
    int update(EvaluationRecord record);

    /**
     * 获取评估记录列表
     *
     * @param query 查询条件
     * @return 评估记录列表
     */
    List<EvaluationRecord> selectListByQuery(EvalQuery query);

}
