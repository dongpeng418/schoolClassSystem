package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.EvalFieldQuery;
import cn.com.school.classinfo.model.SysEvaluationField;

import java.util.List;

public interface SysEvaluationFieldMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysEvaluationField record);

    int insertSelective(SysEvaluationField record);

    SysEvaluationField selectByPrimaryKey(Integer id);

    List<SysEvaluationField> selectListByQuery(EvalFieldQuery query);

    int updateByPrimaryKeySelective(SysEvaluationField record);

    int updateByPrimaryKey(SysEvaluationField record);

    int batchUpdate(List<SysEvaluationField> fieldList);

    int batchInsert(List<SysEvaluationField> fieldList);
}