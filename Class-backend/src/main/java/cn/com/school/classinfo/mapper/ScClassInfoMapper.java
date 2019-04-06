package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScClassInfo;

public interface ScClassInfoMapper {
    int deleteByPrimaryKey(Integer cId);

    int insert(ScClassInfo record);

    int insertSelective(ScClassInfo record);

    ScClassInfo selectByPrimaryKey(Integer cId);

    int updateByPrimaryKeySelective(ScClassInfo record);

    int updateByPrimaryKey(ScClassInfo record);
}