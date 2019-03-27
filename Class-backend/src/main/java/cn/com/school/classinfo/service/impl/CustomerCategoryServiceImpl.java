package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.CustomerCategoryQuery;
import cn.com.school.classinfo.mapper.CustomerCategoryMapper;
import cn.com.school.classinfo.model.CustomerCategory;
import cn.com.school.classinfo.service.CustomerCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户类别服务实现类
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerCategoryServiceImpl implements CustomerCategoryService {

    private final CustomerCategoryMapper customerCategoryMapper;

    public CustomerCategoryServiceImpl(CustomerCategoryMapper customerCategoryMapper) {
        this.customerCategoryMapper = customerCategoryMapper;
    }

    /**
     * 增加客户类别
     *
     * @param customerCategory 客户类别
     */
    @Override
    public void add(CustomerCategory customerCategory) {
        CommonUtil.doEvalCreateUpdateInfo(customerCategory, CreateUpdateEnum.CREATE);
        customerCategoryMapper.insertSelective(customerCategory);
    }

    /**
     * 更新客户类别
     *
     * @param customerCategory 客户类别
     */
    @Override
    public void update(CustomerCategory customerCategory){
        CommonUtil.doEvalCreateUpdateInfo(customerCategory, CreateUpdateEnum.UPDATE);
        customerCategoryMapper.updateByPrimaryKeySelective(customerCategory);
    }

    /**
     * 删除客户类别
     *
     * @param id 客户类别ID
     */
    @Override
    public void delete(Integer id){
        customerCategoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据ID查询客户类别
     *
     * @return 客户类别
     */
    @Override
    public CustomerCategory getById(Integer id){
        return customerCategoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询当前渠道的所有客户类别
     *
     * @return 客户类别列表
     */
    @Override
    public List<CustomerCategory> getListByQuery(){
        CustomerCategoryQuery query = new CustomerCategoryQuery();
        query.setTenantId(CommonUtil.getLoginUser().getTenantId());
        return customerCategoryMapper.selectListByQuery(query);
    }

    /**
     * 分页查询当前渠道的客户类别
     *
     * @return 客户类别列表
     */
    @Override
    public PageInfo<CustomerCategory> getPageableListByQuery(CustomerCategoryQuery query){
        query.setTenantId(CommonUtil.getLoginUser().getTenantId());
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(customerCategoryMapper.selectListByQuery(query));
    }

}
