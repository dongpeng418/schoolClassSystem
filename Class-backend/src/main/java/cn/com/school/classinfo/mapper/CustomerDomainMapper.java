package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.CustomerDomainQuery;
import cn.com.school.classinfo.model.CustomerDomain;
import cn.com.school.classinfo.vo.CustomerDomainVO;

import java.util.List;

public interface CustomerDomainMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(CustomerDomain record);

    int insertSelective(CustomerDomain record);

    CustomerDomain selectByPrimaryKey(Integer id);

    CustomerDomain selectByQuery(CustomerDomainQuery query);

    List<CustomerDomainVO> selectListByQuery(CustomerDomainQuery query);

    int updateByPrimaryKeySelective(CustomerDomain record);

    int updateByPrimaryKey(CustomerDomain record);

    CustomerDomain selectByServerIp(String serverIp);
}