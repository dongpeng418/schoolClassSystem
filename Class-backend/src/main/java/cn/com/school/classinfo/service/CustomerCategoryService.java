package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.CustomerCategoryQuery;
import cn.com.school.classinfo.model.CustomerCategory;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 客户类别service
 *
 * @author dongpp
 * @date 2018/12/13
 */
public interface CustomerCategoryService {

    /**
     * 增加客户类别
     *
     * @param customerCategory 客户类别
     */
    void add(CustomerCategory customerCategory);

    /**
     * 更新客户类别
     *
     * @param customerCategory 客户类别
     */
    void update(CustomerCategory customerCategory);

    /**
     * 删除客户类别
     *
     * @param id 客户类别ID
     */
    void delete(Integer id);

    /**
     * 根据ID查询客户类别
     *
     * @return 客户类别
     */
    CustomerCategory getById(Integer id);

    /**
     * 查询当前渠道的所有的客户类别
     *
     * @return 客户类别列表
     */
    List<CustomerCategory> getListByQuery();

    /**
     * 分页查询当前渠道的客户类别
     *
     * @return 客户类别列表
     */
    PageInfo<CustomerCategory> getPageableListByQuery(CustomerCategoryQuery query);
}
