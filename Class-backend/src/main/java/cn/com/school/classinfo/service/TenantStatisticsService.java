package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.TenantEvalItemsQuery;
import cn.com.school.classinfo.common.query.TenantEvalSummaryQuery;
import cn.com.school.classinfo.vo.TenantEvalItemVO;
import cn.com.school.classinfo.vo.TenantEvalItemsVO;
import cn.com.school.classinfo.vo.TenantEvalSummaryItemVO;
import cn.com.school.classinfo.vo.TenantEvalSummaryVO;

import java.util.List;

/**
 * 查询统计Service
 *
 * @author dongpp
 * @date 2018/11/24
 */
public interface TenantStatisticsService {

    /**
     * 查询渠道-估价汇总信息
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    TenantEvalSummaryVO getSummaryVO(TenantEvalSummaryQuery query);

    /**
     * 查询渠道-估价汇总明细信息
     *
     * @param query 查询条件
     * @return 汇总明细信息
     */
    List<TenantEvalSummaryItemVO> getSummaryItemVO(TenantEvalSummaryQuery query);

    /**
     * 查询渠道-估价明细信息
     *
     * @param query 查询条件
     * @return 明细信息
     */
    TenantEvalItemsVO getItemsVO(TenantEvalItemsQuery query);

    /**
     * 获取估价明细汇总信息-导出数据用
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    List<TenantEvalItemVO> getEvalItemsInfoByExport(TenantEvalItemsQuery query);

}
