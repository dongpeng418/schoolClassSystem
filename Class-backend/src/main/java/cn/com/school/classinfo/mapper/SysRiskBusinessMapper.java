package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysRiskBusiness;

import java.util.List;

public interface SysRiskBusinessMapper {

    int deleteByRiskId(Integer riskId);

    int batchInsert(List<SysRiskBusiness> record);

    SysRiskBusiness selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SysRiskBusiness record);
}