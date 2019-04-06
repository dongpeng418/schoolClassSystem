package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScStudentSelect;

public interface ScStudentSelectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScStudentSelect record);

    int insertSelective(ScStudentSelect record);

    ScStudentSelect selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScStudentSelect record);

    int updateByPrimaryKey(ScStudentSelect record);
}