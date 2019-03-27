package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.CustomerCategoryQuery;
import cn.com.school.classinfo.model.CustomerCategory;

import java.util.List;

public interface CustomerCategoryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(CustomerCategory record);

    int insertSelective(CustomerCategory record);

    CustomerCategory selectByPrimaryKey(Integer id);

    List<CustomerCategory> selectListByQuery(CustomerCategoryQuery query);

    int updateByPrimaryKeySelective(CustomerCategory record);

    int updateByPrimaryKey(CustomerCategory record);
}