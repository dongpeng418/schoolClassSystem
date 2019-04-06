package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScTeacherSubject;

public interface ScTeacherSubjectMapper {
    int deleteByPrimaryKey(Integer tsId);

    int insert(ScTeacherSubject record);

    int insertSelective(ScTeacherSubject record);

    ScTeacherSubject selectByPrimaryKey(Integer tsId);

    int updateByPrimaryKeySelective(ScTeacherSubject record);

    int updateByPrimaryKey(ScTeacherSubject record);
}