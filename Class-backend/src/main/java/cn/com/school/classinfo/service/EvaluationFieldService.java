package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.EvalFieldQuery;
import cn.com.school.classinfo.model.SysEvaluationField;

import java.util.List;

/**
 * 估价字段Service
 *
 * @author dongpp
 * @date 2018/11/22
 */
public interface EvaluationFieldService {

    /**
     * 批量更新估价字段信息
     *
     * @param evaluationFields 估价字段
     */
    void save(List<SysEvaluationField> evaluationFields);

    /**
     * 批量新增估价字段信息
     *
     * @param evaluationFields 估价字段
     */
    void insert(List<SysEvaluationField> evaluationFields);

    /**
     * 获取估价字段列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<SysEvaluationField> list(EvalFieldQuery query);
}
