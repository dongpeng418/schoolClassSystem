package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScStudentInfo;

public interface ScStudentInfoMapper {
    int deleteByPrimaryKey(Integer stuId);

    int insert(ScStudentInfo record);

    int insertSelective(ScStudentInfo record);

    ScStudentInfo selectByPrimaryKey(Integer stuId);

    int updateByPrimaryKeySelective(ScStudentInfo record);

    int updateByPrimaryKey(ScStudentInfo record);
}