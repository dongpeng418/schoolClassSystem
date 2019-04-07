package cn.com.school.classinfo.mapper;

import org.apache.ibatis.annotations.Param;

import cn.com.school.classinfo.model.ScBaseDictionary;

public interface ScBaseDictionaryMapper {
    int deleteByPrimaryKey(Integer bdId);

    int insert(ScBaseDictionary record);

    int insertSelective(ScBaseDictionary record);

    ScBaseDictionary selectByPrimaryKey(Integer bdId);

    int updateByPrimaryKeySelective(ScBaseDictionary record);

    int updateByPrimaryKey(ScBaseDictionary record);

    ScBaseDictionary queryByTypeAndName(@Param("name") String name,@Param("type") String type);
}