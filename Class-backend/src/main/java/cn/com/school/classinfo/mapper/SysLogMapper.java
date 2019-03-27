package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.SysLogQuery;
import cn.com.school.classinfo.model.SysLog;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Integer id);

    List<SysLog> selectListByQuery(SysLogQuery query);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);
}