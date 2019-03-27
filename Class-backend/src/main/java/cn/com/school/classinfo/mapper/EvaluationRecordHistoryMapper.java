package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.EvalQuery;
import cn.com.school.classinfo.model.EvaluationRecordHistory;

import java.util.List;

/**
 * 评估记录Mapper
 *
 * @author dongpp
 * @date 2018/10/24
 */
public interface EvaluationRecordHistoryMapper {

    /**
     * 插入评估记录历史
     *
     * @param record 评估记录历史
     * @return 数量
     */
    int insert(EvaluationRecordHistory record);

    /**
     * 根据估价记录ID查询估价历史列表
     *
     * @param query 查询条件
     * @return 估价历史列表
     */
    List<EvaluationRecordHistory> selectListByQuery(EvalQuery query);

    /**
     * 根据ID查找估价历史信息
     *
     * @param query 查询条件
     * @return 估价历史信息
     */
    EvaluationRecordHistory selectByQuery(EvalQuery query);

    /**
     * 更新估价记录的报告状态
     *
     * @param history 历史信息
     * @return 数量
     */
    int update(EvaluationRecordHistory history);
}
