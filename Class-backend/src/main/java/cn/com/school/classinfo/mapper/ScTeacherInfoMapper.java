package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScTeacherInfo;

public interface ScTeacherInfoMapper {
    int deleteByPrimaryKey(Integer tId);

    int insert(ScTeacherInfo record);

    int insertSelective(ScTeacherInfo record);

    ScTeacherInfo selectByPrimaryKey(Integer tId);

    int updateByPrimaryKeySelective(ScTeacherInfo record);

    int updateByPrimaryKey(ScTeacherInfo record);
}