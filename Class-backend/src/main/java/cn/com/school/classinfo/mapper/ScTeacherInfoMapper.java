package cn.com.school.classinfo.mapper;

import java.util.List;

import cn.com.school.classinfo.model.ScTeacherInfo;

public interface ScTeacherInfoMapper {
    int deleteByPrimaryKey(Integer tId);

    int insert(ScTeacherInfo record);

    int insertSelective(ScTeacherInfo record);

    ScTeacherInfo selectByPrimaryKey(Integer tId);

    int updateByPrimaryKeySelective(ScTeacherInfo record);

    int updateByPrimaryKey(ScTeacherInfo record);

    /**
     * 插入批量教师信息
     * @param batchItems 教师列表
     * @return 数量
     */
    int batchInsert(List<ScTeacherInfo> batchItems);
}