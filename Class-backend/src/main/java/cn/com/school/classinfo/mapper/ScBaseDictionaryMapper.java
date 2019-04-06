package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.ScBaseDictionary;

public interface ScBaseDictionaryMapper {
    int deleteByPrimaryKey(Integer bdId);

    int insert(ScBaseDictionary record);

    int insertSelective(ScBaseDictionary record);

    ScBaseDictionary selectByPrimaryKey(Integer bdId);

    int updateByPrimaryKeySelective(ScBaseDictionary record);

    int updateByPrimaryKey(ScBaseDictionary record);
}