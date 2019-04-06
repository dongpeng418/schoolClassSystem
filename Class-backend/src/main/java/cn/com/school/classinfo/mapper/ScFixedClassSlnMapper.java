package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScFixedClassSln;

public interface ScFixedClassSlnMapper {
    int deleteByPrimaryKey(Integer fcsId);

    int insert(ScFixedClassSln record);

    int insertSelective(ScFixedClassSln record);

    ScFixedClassSln selectByPrimaryKey(Integer fcsId);

    int updateByPrimaryKeySelective(ScFixedClassSln record);

    int updateByPrimaryKey(ScFixedClassSln record);
}