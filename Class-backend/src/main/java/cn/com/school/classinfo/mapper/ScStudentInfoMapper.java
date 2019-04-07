package cn.com.school.classinfo.mapper;

import java.util.List;

import cn.com.school.classinfo.model.ScStudentInfo;

public interface ScStudentInfoMapper {
    int deleteByPrimaryKey(Integer stuId);

    int insert(ScStudentInfo record);

    int insertSelective(ScStudentInfo record);

    ScStudentInfo selectByPrimaryKey(Integer stuId);

    int updateByPrimaryKeySelective(ScStudentInfo record);

    int updateByPrimaryKey(ScStudentInfo record);

    /**
     * 插入批量学生信息
     * @param batchItems 学生列表
     * @return 数量
     */
    int batchInsert(List<ScStudentInfo> batchItems);
}