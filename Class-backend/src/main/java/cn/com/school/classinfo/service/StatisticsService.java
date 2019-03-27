package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.EvalItemsQuery;
import cn.com.school.classinfo.common.query.EvalSummaryQuery;
import cn.com.school.classinfo.vo.EvalItemVO;
import cn.com.school.classinfo.vo.EvalItemsVO;
import cn.com.school.classinfo.vo.EvalSummaryItemVO;
import cn.com.school.classinfo.vo.EvalSummaryVO;
import cn.com.school.classinfo.vo.UserStatisticsVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 查询统计Service
 *
 * @author dongpp
 * @date 2018/11/24
 */
public interface StatisticsService {

    /**
     * 查询估价汇总记录信息
     *
     * @param query 查询条件
     * @return 记录列表
     */
    PageInfo<EvalSummaryItemVO> getOrgSummaryItemListByPage(EvalSummaryQuery query);

    /**
     * 查询估价汇总记录信息-不分页
     *
     * @param query 查询条件
     * @return 记录列表
     */
    List<EvalSummaryItemVO> getOrgSummaryItemList(EvalSummaryQuery query);

    /**
     * 查询估价汇总信息
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    EvalSummaryVO getOrgSummaryInfo(EvalSummaryQuery query);

    /**
     * 查询用户估价汇总记录信息
     *
     * @param query 查询条件
     * @return 记录列表
     */
    PageInfo<EvalSummaryItemVO> getUserSummaryItemListByPage(EvalSummaryQuery query);

    /**
     * 查询用户估价汇总记录信息
     *
     * @param query 查询条件
     * @return 记录列表
     */
    List<EvalSummaryItemVO> getUserSummaryItemList(EvalSummaryQuery query);

    /**
     * 查询用户估价汇总数量
     *
     * @param query 查询条件
     * @return 用户数量
     */
    Integer getUserSummaryTotal(EvalSummaryQuery query);

    /**
     * 获取估价明细汇总信息
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    EvalItemsVO getEvalItemsInfo(EvalItemsQuery query);

    /**
     * 获取估价明细汇总信息-导出数据用
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    List<EvalItemVO> getEvalItemsInfoByExport(EvalItemsQuery query);

    /**
     * 根据登录名获取用户估价统计信息
     *
     * @return 统计信息
     */
    UserStatisticsVO getUserStatistics();
}
