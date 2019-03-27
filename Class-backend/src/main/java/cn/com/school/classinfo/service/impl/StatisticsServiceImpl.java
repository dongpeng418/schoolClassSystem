package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.query.EvalItemsQuery;
import cn.com.school.classinfo.common.query.EvalSummaryQuery;
import cn.com.school.classinfo.mapper.StatisticsMapper;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.StatisticsService;
import cn.com.school.classinfo.vo.EvalItemVO;
import cn.com.school.classinfo.vo.EvalItemsVO;
import cn.com.school.classinfo.vo.EvalSummaryItemVO;
import cn.com.school.classinfo.vo.EvalSummaryVO;
import cn.com.school.classinfo.vo.UserStatisticsVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_EVAL;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_EVAL_RAPID;
/**
 * @author dongpp
 * @date 2018/11/24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;

    public StatisticsServiceImpl(StatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    /**
     * 查询机构估价汇总记录信息
     *
     * @param query 查询条件
     * @return 记录列表
     */
    @Override
    public PageInfo<EvalSummaryItemVO> getOrgSummaryItemListByPage(EvalSummaryQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(statisticsMapper.selectOrgSummaryList(query));
    }

    /**
     * 查询机构估价汇总记录信息-不分页
     *
     * @param query 查询条件
     * @return 记录列表
     */
    @Override
    public List<EvalSummaryItemVO> getOrgSummaryItemList(EvalSummaryQuery query) {
        return statisticsMapper.selectOrgSummaryList(query);
    }

    /**
     * 查询机构估价汇总信息
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    @Override
    public EvalSummaryVO getOrgSummaryInfo(EvalSummaryQuery query) {
        return statisticsMapper.selectOrgSummary(query);
    }


    /**
     * 查询用户估价汇总记录信息
     *
     * @param query 查询条件
     * @return 记录列表
     */
    @Override
    public PageInfo<EvalSummaryItemVO> getUserSummaryItemListByPage(EvalSummaryQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(statisticsMapper.selectUserSummaryList(query));
    }

    /**
     * 查询用户估价汇总记录信息-不分页
     *
     * @param query 查询条件
     * @return 记录列表
     */
    @Override
    public List<EvalSummaryItemVO> getUserSummaryItemList(EvalSummaryQuery query) {
        return statisticsMapper.selectUserSummaryList(query);
    }

    /**
     * 查询用户估价汇总数量
     *
     * @param query 查询条件
     * @return 用户数量
     */
    @Override
    public Integer getUserSummaryTotal(EvalSummaryQuery query) {
        return statisticsMapper.selectUserSummaryTotal(query);
    }

    /**
     * 获取估价明细汇总信息
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    @Override
    public EvalItemsVO getEvalItemsInfo(EvalItemsQuery query) {
        String statMethod = String.valueOf(query.getStatMethod());
        String statSubMethod = query.getStatSubMethod();
        EvalItemsVO itemsVO;
        //按估价方式统计
        if (ITEMS_METHOD_EVAL.equals(statMethod)) {
            //如果子方式为空，则查询全部
            itemsVO = statisticsMapper.selectItemsByEvalTotal(query);
            if(Objects.isNull(itemsVO)){
                return null;
            }
            if (StringUtils.isBlank(query.getStatSubMethod())) {
                Integer recordUsers = statisticsMapper.selectItemsEvalUsers(query);
                PageHelper.startPage(query.getPageIndex(), query.getPageSize());
                PageInfo<EvalItemVO> pageInfo = new PageInfo<>(statisticsMapper.selectItemsByEval(query));
                itemsVO.setPageInfo(pageInfo);
                itemsVO.setUserTotal(recordUsers);
            }
            //查询快速估价
            else if (ITEMS_METHOD_EVAL_RAPID.equals(statSubMethod)) {
                Integer recordUsers = statisticsMapper.selectItemsRecordUsers(query);
                PageHelper.startPage(query.getPageIndex(), query.getPageSize());
                PageInfo<EvalItemVO> pageInfo = new PageInfo<>(statisticsMapper.selectItemsByRapid(query));
                itemsVO.setPageInfo(pageInfo);
                itemsVO.setUserTotal(recordUsers);
                itemsVO.setBatchTotal(null);
            }
            //查询批量估价
            else {
                Integer batchUsers = statisticsMapper.selectItemsBatchUsers(query);
                PageHelper.startPage(query.getPageIndex(), query.getPageSize());
                PageInfo<EvalItemVO> pageInfo = new PageInfo<>(statisticsMapper.selectItemsByBatch(query));
                itemsVO.setPageInfo(pageInfo);
                itemsVO.setUserTotal(batchUsers);
                itemsVO.setRapidTotal(null);
                itemsVO.setHaTotal(null);
            }
        }
        //报告方式统计
        else {
            itemsVO = statisticsMapper.selectItemsByReportTotal(query);
            if(Objects.isNull(itemsVO)){
                return null;
            }
            Integer reportUsers = statisticsMapper.selectItemsReportUsers(query);
            PageHelper.startPage(query.getPageIndex(), query.getPageSize());
            PageInfo<EvalItemVO> pageInfo = new PageInfo<>(statisticsMapper.selectItemsByReport(query));
            itemsVO.setPageInfo(pageInfo);
            itemsVO.setUserTotal(reportUsers);
        }
        return itemsVO;
    }

    /**
     * 获取估价明细汇总信息-导出数据用
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    @Override
    public List<EvalItemVO> getEvalItemsInfoByExport(EvalItemsQuery query) {
        String statMethod = String.valueOf(query.getStatMethod());
        String statSubMethod = query.getStatSubMethod();
        //按估价方式统计
        if (ITEMS_METHOD_EVAL.equals(statMethod)) {
            //如果子方式为空，则查询全部
            if (StringUtils.isBlank(query.getStatSubMethod())) {
                return statisticsMapper.selectItemsByEval(query);
            }
            //查询快速估价
            else if (ITEMS_METHOD_EVAL_RAPID.equals(statSubMethod)) {
                return statisticsMapper.selectItemsByRapid(query);
            }
            //查询批量估价
            else {
                return statisticsMapper.selectItemsByBatch(query);
            }
        }
        //报告方式统计
        else {
            return statisticsMapper.selectItemsByReport(query);
        }
    }

    /**
     * 根据登录名获取用户估价统计信息
     *
     * @return 统计信息
     */
    @Override
    public UserStatisticsVO getUserStatistics() {
        SysUser user = CommonUtil.getLoginUser();
        String loginUser = user.getLoginUser();
        Integer companyId = user.getCustomerCompanyId();
        return statisticsMapper.selectUserStatistics(loginUser, companyId);
    }
}