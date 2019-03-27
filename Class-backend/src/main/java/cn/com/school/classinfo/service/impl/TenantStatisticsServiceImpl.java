package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.query.TenantEvalItemsQuery;
import cn.com.school.classinfo.common.query.TenantEvalSummaryQuery;
import cn.com.school.classinfo.mapper.TenantStatisticsMapper;
import cn.com.school.classinfo.service.TenantStatisticsService;
import cn.com.school.classinfo.vo.TenantEvalItemVO;
import cn.com.school.classinfo.vo.TenantEvalItemsVO;
import cn.com.school.classinfo.vo.TenantEvalSummaryItemVO;
import cn.com.school.classinfo.vo.TenantEvalSummaryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_EVAL;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_EVAL_RAPID;

/**
 * @author dongpp
 * @date 2018/11/24
 */
@Slf4j
@Service
public class TenantStatisticsServiceImpl implements TenantStatisticsService {

    private final TenantStatisticsMapper statisticsMapper;

    public TenantStatisticsServiceImpl(TenantStatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    /**
     * 查询渠道-估价汇总信息
     *
     * @param query 查询条件
     * @return 汇总信息
     */
    @Override
    public TenantEvalSummaryVO getSummaryVO(TenantEvalSummaryQuery query) {
        Integer tenantId = CommonUtil.getTenantId();
        query.setTenantId(tenantId);
        TenantEvalSummaryVO summaryVO = statisticsMapper.selectSummary(query);
        if (Objects.nonNull(summaryVO)) {
            PageHelper.startPage(query.getPageIndex(), query.getPageSize());
            List<TenantEvalSummaryItemVO> itemVOList = statisticsMapper.selectSummaryItem(query);
            summaryVO.setPageInfo(new PageInfo<>(itemVOList));
            summaryVO.setCustomerTotal((int) summaryVO.getPageInfo().getTotal());
        }
        return summaryVO;
    }

    /**
     * 查询渠道-估价汇总明细信息-导出用
     *
     * @param query 查询条件
     * @return 汇总明细信息
     */
    @Override
    public List<TenantEvalSummaryItemVO> getSummaryItemVO(TenantEvalSummaryQuery query) {
        Integer tenantId = CommonUtil.getTenantId();
        query.setTenantId(tenantId);
        return statisticsMapper.selectSummaryItem(query);
    }

    /**
     * 查询渠道-估价明细信息
     *
     * @param query 查询条件
     * @return 明细信息
     */
    @Override
    public TenantEvalItemsVO getItemsVO(TenantEvalItemsQuery query) {
        String statMethod = String.valueOf(query.getStatMethod());
        Integer tenantId = CommonUtil.getTenantId();
        query.setTenantId(tenantId);
        TenantEvalItemsVO itemsVO;
        //按估价方式统计
        if (ITEMS_METHOD_EVAL.equals(statMethod)) {
            //如果子方式为空，则查询全部
            long start = System.currentTimeMillis();
            itemsVO = statisticsMapper.selectItemsByEvalTotal(query);
            long end = System.currentTimeMillis() - start;
            log.info("selectItemsByEvalTotal speed time: {}", end);
            if (StringUtils.isBlank(query.getStatSubMethod())) {
                if (Objects.nonNull(itemsVO)) {
                    Integer batchTotal = statisticsMapper.selectItemsByEvalBatchTotal(query);
                    itemsVO.setBatchTotal(batchTotal);
                    start = System.currentTimeMillis();
                    int pageStart = 0;
                    if(query.getPageIndex() != 1){
                        pageStart = (query.getPageIndex() - 1) * query.getPageSize();
                    }
                    query.setPageStart(pageStart);
                    Integer recordCount = statisticsMapper.selectItemsByEvalRecordCount(query);
                    Integer historyCount = statisticsMapper.selectItemsByEvalHistoryCount(query);
                    Integer batchCount = statisticsMapper.selectItemsByEvalBatchCount(query);
                    int total = (Objects.isNull(recordCount) ? 0 : recordCount)
                            + (Objects.isNull(historyCount) ? 0 : historyCount)
                            + (Objects.isNull(batchCount) ? 0 : batchCount);
                    end = System.currentTimeMillis() - start;
                    log.info("selectItemsByEvalCount speed time: {}", end);
                    start = System.currentTimeMillis();
                    PageInfo<TenantEvalItemVO> pageInfo = new PageInfo<>(statisticsMapper.selectItemsByEval(query));
                    pageInfo.setPages(total/query.getPageSize());
                    pageInfo.setPageNum(query.getPageIndex());
                    pageInfo.setPageSize(query.getPageSize());
                    pageInfo.setTotal(total);
                    end = System.currentTimeMillis() - start;
                    log.info("selectItemsByEval speed time: {}", end);
                    start = System.currentTimeMillis();
                    List<String> recordUsers = statisticsMapper.selectItemsEvalRecordUsers(query);
                    List<String> batchUsers = statisticsMapper.selectItemsEvalBatchUsers(query);
                    Set<String> users = Sets.newHashSet();
                    if(CollectionUtils.isNotEmpty(recordUsers)){
                        users.addAll(recordUsers);
                    }
                    if(CollectionUtils.isNotEmpty(batchUsers)){
                        users.addAll(batchUsers);
                    }
                    end = System.currentTimeMillis() - start;
                    log.info("selectItemsEvalUsers speed time: {}", end);
                    start = System.currentTimeMillis();
                    List<Integer> recordCompany = statisticsMapper.selectItemsEvalRecordCompany(query);
                    List<Integer> batchCompany = statisticsMapper.selectItemsEvalBatchCompany(query);
                    Set<Integer> companys = Sets.newHashSet();
                    if(CollectionUtils.isNotEmpty(recordCompany)){
                        companys.addAll(recordCompany);
                    }
                    if(CollectionUtils.isNotEmpty(batchCompany)){
                        companys.addAll(batchCompany);
                    }
                    end = System.currentTimeMillis() - start;
                    log.info("selectItemsEvalCompany speed time: {}", end);
                    itemsVO.setPageInfo(pageInfo);
                    itemsVO.setUserTotal(users.size());
                    itemsVO.setCustomerTotal(companys.size());
                }
            }
            //查询快速估价
            else if (ITEMS_METHOD_EVAL_RAPID.equals(query.getStatSubMethod())) {
                if (Objects.nonNull(itemsVO)) {
                    PageHelper.startPage(query.getPageIndex(), query.getPageSize());
                    PageInfo<TenantEvalItemVO> pageInfo = new PageInfo<>(statisticsMapper.selectItemsByRapid(query));
                    Integer recordUsers = statisticsMapper.selectItemsRecordUsers(query);
                    Integer recordCompany = statisticsMapper.selectItemsRecordCompany(query);
                    itemsVO.setPageInfo(pageInfo);
                    itemsVO.setUserTotal(recordUsers);
                    itemsVO.setCustomerTotal(recordCompany);
                }
            }
            //查询批量估价
            else {
                if (Objects.nonNull(itemsVO)) {
                    Integer total = statisticsMapper.selectItemsByEvalBatchCount(query);
                    int pageStart = 0;
                    if(query.getPageIndex() != 1){
                        pageStart = (query.getPageIndex() - 1) * query.getPageSize();
                    }
                    query.setPageStart(pageStart);
                    PageInfo<TenantEvalItemVO> pageInfo = new PageInfo<>(statisticsMapper.selectItemsByBatch(query));
                    pageInfo.setPages(total/query.getPageSize());
                    pageInfo.setPageNum(query.getPageIndex());
                    pageInfo.setPageSize(query.getPageSize());
                    pageInfo.setTotal(total);
                    List<String> batchUsers = statisticsMapper.selectItemsEvalBatchUsers(query);
                    List<Integer> batchCompany = statisticsMapper.selectItemsEvalBatchCompany(query);
                    itemsVO.setPageInfo(pageInfo);
                    if(CollectionUtils.isNotEmpty(batchUsers)){
                        itemsVO.setUserTotal(batchUsers.size());
                    }
                    if(CollectionUtils.isNotEmpty(batchCompany)){
                        itemsVO.setCustomerTotal(batchCompany.size());
                    }
                    Integer batchTotal = statisticsMapper.selectItemsByEvalBatchTotal(query);
                    itemsVO.setBatchTotal(batchTotal);
                }
            }
        }
        //报告方式统计
        else {
            itemsVO = statisticsMapper.selectItemsByReportTotal(query);
            if (Objects.nonNull(itemsVO)) {
                PageHelper.startPage(query.getPageIndex(), query.getPageSize());
                PageInfo<TenantEvalItemVO> pageInfo = new PageInfo<>(statisticsMapper.selectItemsByReport(query));
                Integer reportUsers = statisticsMapper.selectItemsReportUsers(query);
                Integer reportCompany = statisticsMapper.selectItemsReportCompany(query);
                itemsVO.setPageInfo(pageInfo);
                itemsVO.setUserTotal(reportUsers);
                itemsVO.setCustomerTotal(reportCompany);
            }
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
    public List<TenantEvalItemVO> getEvalItemsInfoByExport(TenantEvalItemsQuery query) {
        Integer tenantId = CommonUtil.getTenantId();
        query.setTenantId(tenantId);
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
}