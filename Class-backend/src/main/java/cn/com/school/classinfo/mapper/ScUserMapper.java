package cn.com.school.classinfo.mapper;

import java.util.List;

import cn.com.school.classinfo.model.ScUser;

public interface ScUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScUser record);

    int insertSelective(ScUser record);

    ScUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScUser record);

    int updateByPrimaryKey(ScUser record);

    /**
     * 插入批量学生用户信息
     * @param batchItems 学生用户列表
     * @return 数量
     */
    int batchInsert(List<ScUser> batchItems);
}