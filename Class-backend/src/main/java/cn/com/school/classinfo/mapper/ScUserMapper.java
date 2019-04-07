package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScUser;

public interface ScUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScUser record);

    int insertSelective(ScUser record);

    ScUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScUser record);

    int updateByPrimaryKey(ScUser record);
}