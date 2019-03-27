package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.CustomerDomainQuery;
import cn.com.school.classinfo.mapper.CustomerDomainMapper;
import cn.com.school.classinfo.model.CustomerDomain;
import cn.com.school.classinfo.service.CustomerDomainService;
import cn.com.school.classinfo.vo.CustomerDomainVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户域名服务实现类
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerDomainServiceImpl implements CustomerDomainService {

    private final CustomerDomainMapper customerDomainMapper;

    public CustomerDomainServiceImpl(CustomerDomainMapper customerDomainMapper) {
        this.customerDomainMapper = customerDomainMapper;
    }

    /**
     * 增加客户域名
     *
     * @param customerDomain 客户域名
     */
    @Override
    public void add(CustomerDomain customerDomain) {
        Integer companyId = customerDomain.getCompanyId();
        CommonUtil.doEvalCreateUpdateInfo(customerDomain, CreateUpdateEnum.CREATE);
        customerDomain.setCompanyId(companyId);
        customerDomainMapper.insertSelective(customerDomain);
    }

    /**
     * 更新客户域名
     *
     * @param customerDomain 客户域名
     */
    @Override
    public void update(CustomerDomain customerDomain) {
        CommonUtil.doEvalCreateUpdateInfo(customerDomain, CreateUpdateEnum.UPDATE);
        customerDomainMapper.updateByPrimaryKeySelective(customerDomain);
    }

    /**
     * 根据域名url来查询记录
     *
     * @param query 查询条件
     * @return 域名信息
     */
    @Override
    public CustomerDomain getByQuery(CustomerDomainQuery query) {
        query.setTenantId(CommonUtil.getTenantId());
        return customerDomainMapper.selectByQuery(query);
    }

    /**
     * 根据域名ID来查询记录
     *
     * @param id 域名ID
     * @return 域名信息
     */
    @Override
    public CustomerDomain getById(Integer id) {
        return customerDomainMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除客户域名
     *
     * @param id 客户域名ID
     */
    @Override
    public void delete(Integer id) {
        customerDomainMapper.deleteByPrimaryKey(id);
    }

    /**
     * 分页查询当前渠道的客户域名
     *
     * @return 客户域名列表
     */
    @Override
    public PageInfo<CustomerDomainVO> getPageableListByQuery(CustomerDomainQuery query) {
        query.setTenantId(CommonUtil.getLoginUser().getTenantId());
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(customerDomainMapper.selectListByQuery(query));
    }

    /**
     * 根据服务器IP得到相关配置
     *
     * @param serverIp 查询条件
     * @return 域名信息
     */
    @Override
    public CustomerDomain getByServerIp(String serverIp) {
        return customerDomainMapper.selectByServerIp(serverIp);
    }

}
