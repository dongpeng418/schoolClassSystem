package cn.com.school.classinfo.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.com.school.classinfo.common.query.CustomerCompanyQuery;
import cn.com.school.classinfo.model.CustomerCompany;
import cn.com.school.classinfo.vo.CustomerCompanyVO;

/**
 * 客户公司service
 *
 * @author dongpp
 * @date 2018/12/13
 */
public interface CustomerCompanyService {

    /**
     * 增加客户公司
     *
     * @param customerCategory 客户公司
     */
    void add(CustomerCompany customerCategory);

    /**
     * 更新客户公司
     *
     * @param customerCategory 客户公司
     */
    void update(CustomerCompany customerCategory);

    /**
     * 更新营业执照路径
     *
     * @param customerCompany 客户公司
     */
    void updatePath(CustomerCompany customerCompany);

    /**
     * 删除客户公司
     *
     * @param id 客户公司ID
     */
    void delete(Integer id);

    /**
     * 根据公司名称获取信息
     * 用于增加客户公司时验证是否重复
     *
     * @param companyName 公司名称
     * @return 公司信息
     */
    CustomerCompany getByName(String companyName);

    /**
     * 根据ID获取信息
     *
     * @param id ID
     * @return 公司信息
     */
    CustomerCompany getById(Integer id);

    /**
     * 查询当前渠道的所有的客户公司
     *
     * @return 客户公司列表
     */
    List<CustomerCompany> getListByQuery();


    /**
     * 查询未创建客户账户得客户公司
     * @return
     */
    List<CustomerCompanyVO> getNoCreatedUserListByQuery();

    /**
     * 获取未添加域名的客户公司列表
     *
     * @return 客户公司列表
     */
    List<CustomerCompany> getFilterListByQuery();

    /**
     * 分页查询当前渠道的客户公司
     *
     * @return 客户公司列表
     */
    PageInfo<CustomerCompanyVO> getPageableListByQuery(CustomerCompanyQuery query);
}
