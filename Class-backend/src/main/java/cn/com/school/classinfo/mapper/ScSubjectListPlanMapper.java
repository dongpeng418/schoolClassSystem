package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScSubjectListPlan;

public interface ScSubjectListPlanMapper {
    int deleteByPrimaryKey(Integer plsId);

    int insert(ScSubjectListPlan record);

    int insertSelective(ScSubjectListPlan record);

    ScSubjectListPlan selectByPrimaryKey(Integer plsId);

    int updateByPrimaryKeySelective(ScSubjectListPlan record);

    int updateByPrimaryKey(ScSubjectListPlan record);
}