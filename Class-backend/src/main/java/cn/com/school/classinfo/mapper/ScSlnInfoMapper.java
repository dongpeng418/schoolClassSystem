package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScSlnInfo;

public interface ScSlnInfoMapper {
    int deleteByPrimaryKey(Integer sId);

    int insert(ScSlnInfo record);

    int insertSelective(ScSlnInfo record);

    ScSlnInfo selectByPrimaryKey(Integer sId);

    int updateByPrimaryKeySelective(ScSlnInfo record);

    int updateByPrimaryKey(ScSlnInfo record);
}