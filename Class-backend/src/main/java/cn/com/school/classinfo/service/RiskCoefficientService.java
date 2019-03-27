package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.RiskQuery;
import cn.com.school.classinfo.model.SysRiskCoefficient;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 风控系数Service
 *
 * @author dongpp
 * @date 2018/11/21
 */
public interface RiskCoefficientService {

    /**
     * 增加风控系数信息
     *
     * @param coefficient 风控系数
     */
    void add(SysRiskCoefficient coefficient);

    /**
     * 保存风控系数信息
     *
     * @param coefficient 风控系数
     */
    void save(SysRiskCoefficient coefficient);

    /**
     * 删除风控系数信息
     *
     * @param coefficient 风控系数
     */
    void delete(SysRiskCoefficient coefficient);

    /**
     * 查询风控系数列表
     *
     * @param query 查询条件
     * @return 列表
     */
    PageInfo<SysRiskCoefficient> list(RiskQuery query);

    /**
     * 查询所有风控系数列表
     *
     * @return 列表
     */
    List<SysRiskCoefficient> listAll();

    /**
     * 根据区域查询风控系数
     *
     * @return 风控系数
     */
    SysRiskCoefficient getByRegionQuery(RiskQuery query);
}
