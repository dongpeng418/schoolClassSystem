package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.TenantEvalItemsQuery;
import cn.com.school.classinfo.common.query.TenantEvalSummaryQuery;
import cn.com.school.classinfo.vo.TenantEvalItemVO;
import cn.com.school.classinfo.vo.TenantEvalItemsVO;
import cn.com.school.classinfo.vo.TenantEvalSummaryItemVO;
import cn.com.school.classinfo.vo.TenantEvalSummaryVO;

import java.util.List;

/**
 * 统计Mapper
 *
 * @author dongpp
 * @date 2018/11/23
 */
public interface TenantStatisticsMapper {


    /**
     * 根据机构查询估价汇总信息（总览）
     *
     * @param tenantId 渠道ID
     * @param userType 用户类型
     * @return 汇总列表
     */
    Integer selectCustomerCount(Integer tenantId, Integer userType);

    /**
     * 查询渠道-估价明细汇总信息
     *
     * @return
     */
    TenantEvalSummaryVO selectSummary(TenantEvalSummaryQuery query);

    /**
     * 查询渠道-估价明细汇总明细信息
     *
     * @return
     */
    List<TenantEvalSummaryItemVO> selectSummaryItem(TenantEvalSummaryQuery query);

    /**
     * 查询估价明细-估价方式（总览）小区数及快速估价数
     *
     * @param query 查询条件
     * @return 估价明细
     */
    TenantEvalItemsVO selectItemsByEvalTotal(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-估价方式（总览）批量估价
     *
     * @param query 查询条件
     * @return 估价明细
     */
    Integer selectItemsByEvalBatchTotal(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-估价方式 快速估价数量
     *
     * @param query 查询条件
     * @return 估价明细
     */
    Integer selectItemsByEvalRecordCount(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-估价方式 估价历史数量
     *
     * @param query 查询条件
     * @return 估价明细
     */
    Integer selectItemsByEvalHistoryCount(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-估价方式 批量估价数量
     *
     * @param query 查询条件
     * @return 估价明细
     */
    Integer selectItemsByEvalBatchCount(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-估价方式
     *
     * @param query 查询条件
     * @return 估价明细
     */
    List<TenantEvalItemVO> selectItemsByEval(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    List<String> selectItemsEvalRecordUsers(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    List<String> selectItemsEvalBatchUsers(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    Integer selectItemsRecordUsers(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    Integer selectItemsReportUsers(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    Integer selectItemsBatchUsers(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    List<Integer> selectItemsEvalRecordCompany(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    List<Integer> selectItemsEvalBatchCompany(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    Integer selectItemsRecordCompany(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    Integer selectItemsReportCompany(TenantEvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    Integer selectItemsBatchCompany(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-快速估价
     *
     * @param query 查询条件
     * @return 估价明细
     */
    List<TenantEvalItemVO> selectItemsByRapid(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-批量估价
     *
     * @param query 查询条件
     * @return 估价明细
     */
    List<TenantEvalItemVO> selectItemsByBatch(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-估价报告（总览）
     *
     * @param query 查询条件
     * @return 总览信息
     */
    TenantEvalItemsVO selectItemsByReportTotal(TenantEvalItemsQuery query);

    /**
     * 查询估价明细-估价报告
     *
     * @param query 查询条件
     * @return 估价明细
     */
    List<TenantEvalItemVO> selectItemsByReport(TenantEvalItemsQuery query);
}
