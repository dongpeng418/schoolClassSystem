package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScMoveClassSln;

public interface ScMoveClassSlnMapper {
    int deleteByPrimaryKey(Integer mcsId);

    int insert(ScMoveClassSln record);

    int insertSelective(ScMoveClassSln record);

    ScMoveClassSln selectByPrimaryKey(Integer mcsId);

    int updateByPrimaryKeySelective(ScMoveClassSln record);

    int updateByPrimaryKey(ScMoveClassSln record);
}