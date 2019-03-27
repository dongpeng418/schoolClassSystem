package cn.com.school.classinfo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.CustomerCompanyQuery;
import cn.com.school.classinfo.mapper.CustomerCompanyMapper;
import cn.com.school.classinfo.model.CustomerCompany;
import cn.com.school.classinfo.service.CustomerCompanyService;
import cn.com.school.classinfo.vo.CustomerCompanyVO;

/**
 * 客户公司服务实现类
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerCompanyServiceImpl implements CustomerCompanyService {

    private final CustomerCompanyMapper customerCompanyMapper;

    public CustomerCompanyServiceImpl(CustomerCompanyMapper customerCompanyMapper) {
        this.customerCompanyMapper = customerCompanyMapper;
    }


    /**
     * 增加客户公司
     *
     * @param customerCompany 客户公司
     */
    @Override
    public void add(CustomerCompany customerCompany) {
        CommonUtil.doEvalCreateUpdateInfo(customerCompany, CreateUpdateEnum.CREATE);
        customerCompanyMapper.insertSelective(customerCompany);
    }

    /**
     * 更新客户公司
     *
     * @param customerCompany 客户公司
     */
    @Override
    public void update(CustomerCompany customerCompany){
        CommonUtil.doEvalCreateUpdateInfo(customerCompany, CreateUpdateEnum.UPDATE);
        customerCompanyMapper.updateByPrimaryKeySelective(customerCompany);
    }

    /**
     * 更新营业执照路径
     *
     * @param customerCompany 客户公司
     */
    @Override
    public void updatePath(CustomerCompany customerCompany){
        CommonUtil.doEvalCreateUpdateInfo(customerCompany, CreateUpdateEnum.UPDATE);
        customerCompanyMapper.updatePath(customerCompany);
    }

    /**
     * 删除客户公司
     *
     * @param id 客户公司ID
     */
    @Override
    public void delete(Integer id){
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setId(id);
        CommonUtil.doEvalCreateUpdateInfo(customerCompany, CreateUpdateEnum.UPDATE);
        customerCompanyMapper.logicDelete(customerCompany);
    }

    /**
     * 根据公司名称获取信息
     * 用于增加客户公司时验证是否重复
     *
     * @param companyName 公司名称
     * @return 公司信息
     */
    @Override
    public CustomerCompany getByName(String companyName){
        CustomerCompanyQuery query = new CustomerCompanyQuery();
        query.setTenantId(CommonUtil.getLoginUser().getTenantId());
        query.setCompanyName(companyName);
        return customerCompanyMapper.selectByName(query);
    }

    /**
     * 根据ID获取信息
     *
     * @param id ID
     * @return 公司信息
     */
    @Override
    public CustomerCompany getById(Integer id){
        CustomerCompanyQuery query = new CustomerCompanyQuery();
        query.setTenantId(CommonUtil.getLoginUser().getTenantId());
        return customerCompanyMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询当前渠道的所有客户公司
     *
     * @return 客户公司列表
     */
    @Override
    public List<CustomerCompany> getListByQuery(){
        CustomerCompanyQuery query = new CustomerCompanyQuery();
        query.setTenantId(CommonUtil.getLoginUser().getTenantId());
        return customerCompanyMapper.selectAllByQuery(query);
    }

    /**
     * 获取未添加域名的客户公司列表
     *
     * @return 客户公司列表
     */
    @Override
    public List<CustomerCompany> getFilterListByQuery(){
        CustomerCompanyQuery query = new CustomerCompanyQuery();
        query.setTenantId(CommonUtil.getLoginUser().getTenantId());
        return customerCompanyMapper.selectByFilter(query);
    }

    /**
     * 分页查询当前渠道的客户公司
     *
     * @return 客户公司列表
     */
    @Override
    public PageInfo<CustomerCompanyVO> getPageableListByQuery(CustomerCompanyQuery query){
        query.setTenantId(CommonUtil.getLoginUser().getTenantId());
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(customerCompanyMapper.selectByQuery(query));
    }


    @Override
    public List<CustomerCompanyVO> getNoCreatedUserListByQuery() {
        return customerCompanyMapper.selectNoCreatedUserByQuery(CommonUtil.getLoginUser().getTenantId());
    }

}
