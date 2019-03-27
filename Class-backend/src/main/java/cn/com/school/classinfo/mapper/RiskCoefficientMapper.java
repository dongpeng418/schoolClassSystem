package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.RiskQuery;
import cn.com.school.classinfo.model.SysRiskCoefficient;

import java.util.List;

/**
 * 风控系数Mapper
 *
 * @author dongpp
 * @date 2018/11/21
 */
public interface RiskCoefficientMapper {

    /**
     * 插入风控系数信息
     *
     * @param coefficient 风控系数
     * @return 数量
     */
    int insert(SysRiskCoefficient coefficient);

    /**
     * 更新风控系数信息
     *
     * @param coefficient 风控系数
     * @return 数量
     */
    int update(SysRiskCoefficient coefficient);

    /**
     * 删除风控系数
     *
     * @param coefficient 风控系数
     * @return 数量
     */
    int delete(SysRiskCoefficient coefficient);

    /**
     * 查询风控系数列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<SysRiskCoefficient> selectListByQuery(RiskQuery query);

    /**
     * 根据区域查询风控系数
     *
     * @param query 查询条件
     * @return 风控系数
     */
    SysRiskCoefficient selectByRegionQuery(RiskQuery query);
}
