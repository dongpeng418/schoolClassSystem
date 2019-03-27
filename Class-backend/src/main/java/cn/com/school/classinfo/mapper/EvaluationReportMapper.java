package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.EvalQuery;
import cn.com.school.classinfo.model.EvaluationReport;

/**
 * 评估报告Mapper
 *
 * @author dongpp
 * @date 2018/10/25
 */
public interface EvaluationReportMapper {

    /**
     * 插入评估报告
     *
     * @param report 评估报告
     * @return 数量
     */
    int insert(EvaluationReport report);

    /**
     * 更新估价报告文件路径
     *
     * @param report 报告信息
     * @return 数量
     */
    int update(EvaluationReport report);

    /**
     * 根据估价记录唯一值和报告类型查询报告信息
     *
     * @param query 查询条件
     * @return 报告信息
     */
    EvaluationReport selectByQuery(EvalQuery query);
}
