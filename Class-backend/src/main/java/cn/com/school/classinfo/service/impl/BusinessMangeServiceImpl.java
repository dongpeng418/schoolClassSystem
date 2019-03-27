package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.query.BaseQuery;
import cn.com.school.classinfo.mapper.BusinessMangeMapper;
import cn.com.school.classinfo.model.SysBusinessManage;
import cn.com.school.classinfo.service.BusinessMangeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务管理服务实现类
 *
 * @author dongpp
 * @date 2018/11/21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessMangeServiceImpl implements BusinessMangeService {

    private final BusinessMangeMapper businessMangeMapper;

    @Autowired
    public BusinessMangeServiceImpl(BusinessMangeMapper businessMangeMapper) {
        this.businessMangeMapper = businessMangeMapper;
    }

    /**
     * 增加业务管理信息
     *
     * @param manage 业务管理信息
     */
    @Override
    public void add(SysBusinessManage manage) {
        businessMangeMapper.insert(manage);
    }

    /**
     * 保存业务管理信息
     *
     * @param manage 业务管理信息
     */
    @Override
    public void save(SysBusinessManage manage) {
        businessMangeMapper.update(manage);
    }

    /**
     * 删除业务管理信息
     *
     * @param manage 业务管理信息
     */
    @Override
    public void delete(SysBusinessManage manage) {
        businessMangeMapper.delete(manage);
    }

    /**
     * 根据名称获取业务管理信息
     *
     * @param name 业务名称
     */
    @Override
    public SysBusinessManage getByName(String name) {
        SysBusinessManage manage = new SysBusinessManage();
        manage.setBusinessName(name);
        manage.setCompanyId(CommonUtil.getLoginUser().getCustomerCompanyId());
        return businessMangeMapper.selectByName(manage);
    }

    /**
     * 根据多个ID获取业务管理列表
     *
     * @param ids id数组
     * @return 业务管理列表
     */
    @Override
    public List<SysBusinessManage> getByIds(String[] ids) {
        return businessMangeMapper.selectByIds(ids);
    }

    /**
     * 获取业务管理信息列表
     *
     * @return 列表
     */
    @Override
    public List<SysBusinessManage> list() {
        return businessMangeMapper.selectByCompanyId(CommonUtil.getLoginUser().getCustomerCompanyId());
    }

    /**
     * 获取业务管理信息列表
     *
     * @return 列表
     */
    @Override
    public PageInfo<SysBusinessManage> pageable(BaseQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(businessMangeMapper.selectByCompanyId(CommonUtil.getLoginUser().getCustomerCompanyId()));
    }
}
