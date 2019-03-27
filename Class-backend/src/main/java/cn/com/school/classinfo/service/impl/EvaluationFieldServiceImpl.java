package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.query.EvalFieldQuery;
import cn.com.school.classinfo.mapper.SysEvaluationFieldMapper;
import cn.com.school.classinfo.model.SysEvaluationField;
import cn.com.school.classinfo.service.EvaluationFieldService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author dongpp
 * @date 2018/11/22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EvaluationFieldServiceImpl implements EvaluationFieldService {

    private final SysEvaluationFieldMapper evaluationFieldMapper;

    public EvaluationFieldServiceImpl(SysEvaluationFieldMapper evaluationFieldMapper) {
        this.evaluationFieldMapper = evaluationFieldMapper;
    }

    /**
     * 批量更新估价字段信息
     *
     * @param evaluationFields 估价字段
     */
    @Override
    public void save(List<SysEvaluationField> evaluationFields) {
        evaluationFieldMapper.batchUpdate(evaluationFields);
    }

    /**
     * 批量新增估价字段信息
     *
     * @param evaluationFields 估价字段
     */
    @Override
    public void insert(List<SysEvaluationField> evaluationFields) {
        evaluationFieldMapper.batchInsert(evaluationFields);
    }

    /**
     * 获取估价字段列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public List<SysEvaluationField> list(EvalFieldQuery query) {
        return evaluationFieldMapper.selectListByQuery(query);
    }
}
