package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.CustomerDomainQuery;
import cn.com.school.classinfo.model.CustomerDomain;
import cn.com.school.classinfo.vo.CustomerDomainVO;
import com.github.pagehelper.PageInfo;

/**
 * 客户类别service
 *
 * @author dongpp
 * @date 2018/12/13
 */
public interface CustomerDomainService {

    /**
     * 增加客户类别
     *
     * @param customerDomain 客户类别
     */
    void add(CustomerDomain customerDomain);

    /**
     * 更新客户类别
     *
     * @param customerDomain 客户类别
     */
    void update(CustomerDomain customerDomain);

    /**
     * 根据域名url来查询记录
     *
     * @param query 查询条件
     * @return 域名信息
     */
    CustomerDomain getByQuery(CustomerDomainQuery query);

    /**
     * 根据域名ID来查询记录
     *
     * @param id 域名ID
     * @return 域名信息
     */
    CustomerDomain getById(Integer id);

    /**
     * 删除客户类别
     *
     * @param id 客户类别ID
     */
    void delete(Integer id);

    /**
     * 分页查询当前渠道的客户类别
     *
     * @return 客户类别列表
     */
    PageInfo<CustomerDomainVO> getPageableListByQuery(CustomerDomainQuery query);

    /**
     * @param serverIp
     * @return
     */
    CustomerDomain getByServerIp(String serverIp);
}
