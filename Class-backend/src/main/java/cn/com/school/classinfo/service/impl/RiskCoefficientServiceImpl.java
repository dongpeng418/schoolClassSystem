package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.RiskQuery;
import cn.com.school.classinfo.mapper.RiskCoefficientMapper;
import cn.com.school.classinfo.mapper.SysRiskBusinessMapper;
import cn.com.school.classinfo.model.SysRiskBusiness;
import cn.com.school.classinfo.model.SysRiskCoefficient;
import cn.com.school.classinfo.service.RiskCoefficientService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 风控系数服务实现类
 *
 * @author dongpp
 * @date 2018/11/21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RiskCoefficientServiceImpl implements RiskCoefficientService {

    private final RiskCoefficientMapper riskCoefficientMapper;

    private final SysRiskBusinessMapper sysRiskBusinessMapper;

    public RiskCoefficientServiceImpl(RiskCoefficientMapper riskCoefficientMapper,
                                      SysRiskBusinessMapper sysRiskBusinessMapper) {
        this.riskCoefficientMapper = riskCoefficientMapper;
        this.sysRiskBusinessMapper = sysRiskBusinessMapper;
    }

    /**
     * 增加风控系数信息
     *
     * @param coefficient 风控系数
     */
    @Override
    public void add(SysRiskCoefficient coefficient) {
        riskCoefficientMapper.insert(coefficient);
        //增加中间表信息
        batchInsertRiskBusiness(coefficient);
    }

    /**
     * 保存风控系数信息
     *
     * @param coefficient 风控系数
     */
    @Override
    public void save(SysRiskCoefficient coefficient) {
        riskCoefficientMapper.update(coefficient);

        //删除中间表信息
        sysRiskBusinessMapper.deleteByRiskId(coefficient.getId());
        //增加中间表信息
        batchInsertRiskBusiness(coefficient);
    }

    /**
     * 删除风控系数信息
     *
     * @param coefficient 风控系数
     */
    @Override
    public void delete(SysRiskCoefficient coefficient) {
        riskCoefficientMapper.delete(coefficient);
    }

    /**
     * 查询风控系数列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public PageInfo<SysRiskCoefficient> list(RiskQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(riskCoefficientMapper.selectListByQuery(query));
    }

    /**
     * 查询所有风控系数列表
     *
     * @return 列表
     */
    @Override
    public List<SysRiskCoefficient> listAll() {
        RiskQuery query = new RiskQuery();
        query.authInfo();
        return riskCoefficientMapper.selectListByQuery(query);
    }

    /**
     * 根据区域查询风控系数
     *
     * @return 风控系数
     */
    @Override
    public SysRiskCoefficient getByRegionQuery(RiskQuery query) {
        return riskCoefficientMapper.selectByRegionQuery(query);
    }

    /**
     * 保存风控系数与业务类型的中间表信息
     *
     * @param coefficient 风控系数
     */
    private void batchInsertRiskBusiness(SysRiskCoefficient coefficient){
        if(StringUtils.isBlank(coefficient.getBusinessId())){
            return;
        }
        String[] ids = coefficient.getBusinessId().split(",");
        List<SysRiskBusiness> riskBusinesses = Lists.newArrayList();
        SysRiskBusiness riskBusiness;
        for(String id : ids){
            riskBusiness = new SysRiskBusiness();
            riskBusiness.setRiskId(coefficient.getId());
            riskBusiness.setBusinessId(Integer.valueOf(id));
            CommonUtil.doEvalCreateUpdateInfo(riskBusiness, CreateUpdateEnum.CREATE);
            riskBusinesses.add(riskBusiness);
        }
        sysRiskBusinessMapper.batchInsert(riskBusinesses);
    }
}
