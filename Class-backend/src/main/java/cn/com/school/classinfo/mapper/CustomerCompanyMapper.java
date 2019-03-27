package cn.com.school.classinfo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.school.classinfo.common.query.CustomerCompanyQuery;
import cn.com.school.classinfo.model.CustomerCompany;
import cn.com.school.classinfo.vo.CustomerCompanyVO;

public interface CustomerCompanyMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(CustomerCompany record);

    int insertSelective(CustomerCompany record);

    CustomerCompany selectByPrimaryKey(Integer id);

    CustomerCompany selectByName(CustomerCompanyQuery query);

    List<CustomerCompany> selectAllByQuery(CustomerCompanyQuery query);

    List<CustomerCompanyVO> selectNoCreatedUserByQuery(@Param("tenantId") int tenantId);

    List<CustomerCompany> selectByFilter(CustomerCompanyQuery query);

    List<CustomerCompanyVO> selectByQuery(CustomerCompanyQuery query);

    int updateByPrimaryKeySelective(CustomerCompany record);

    int updatePath(CustomerCompany record);

    int logicDelete(CustomerCompany record);
}