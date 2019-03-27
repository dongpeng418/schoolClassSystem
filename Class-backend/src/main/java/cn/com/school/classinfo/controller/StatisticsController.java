package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.EvalConstant;
import cn.com.school.classinfo.common.query.EvalItemsQuery;
import cn.com.school.classinfo.common.query.EvalSummaryQuery;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.dto.excel.EvalStatisticsItem;
import cn.com.school.classinfo.dto.excel.EvalStatisticsItemReport;
import cn.com.school.classinfo.dto.excel.EvalSummaryOrgItem;
import cn.com.school.classinfo.dto.excel.EvalSummaryUserItem;
import cn.com.school.classinfo.service.StatisticsService;
import cn.com.school.classinfo.utils.ExportUtil;
import cn.com.school.classinfo.vo.EvalItemVO;
import cn.com.school.classinfo.vo.EvalItemsVO;
import cn.com.school.classinfo.vo.EvalSummaryItemVO;
import cn.com.school.classinfo.vo.EvalSummaryVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_EVAL;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_EVAL_BATCH;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_EVAL_RAPID;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_FILENAME;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_REPORT_ADVISORY;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_REPORT_INQUIRY;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.SUMMARY_METHOD_ORG;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.SUMMARY_METHOD_ORG_FILENAME;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.SUMMARY_METHOD_USER_FILENAME;

/**
 * 估价统计接口
 *
 * @author dongpp
 * @date 2018/11/20
 */
@Api(tags = "查询统计接口", position = 8)
@Validated
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final AvsConfig avsConfig;

    private final StatisticsService statisticsService;

    public StatisticsController(AvsConfig avsConfig,
                                StatisticsService statisticsService) {
        this.avsConfig = avsConfig;
        this.statisticsService = statisticsService;
    }

    @ApiOperation(value = "估价汇总表")
    @GetMapping("/summary")
    public ResultMessage evaluationSummary(@Valid @ModelAttribute EvalSummaryQuery query) {
        if (ArrayUtils.isEmpty(query.getOrganizationIds())) {
            return ResultMessage.success();
        }
        String statMethod = String.valueOf(query.getStatMethod());
        EvalSummaryVO summaryVO;
        if("2".equals(query.getSource()) || "3".equals(query.getSource())){
            summaryVO = new EvalSummaryVO();
            summaryVO.setOrganizationTotal(query.getOrganizationIds().length);
            return ResultMessage.success(summaryVO);
        }
        summaryVO = statisticsService.getOrgSummaryInfo(query);
        if (Objects.nonNull(summaryVO)) {
            if (SUMMARY_METHOD_ORG.equals(statMethod)) {
                PageInfo<EvalSummaryItemVO> info = statisticsService.getOrgSummaryItemListByPage(query);
                summaryVO.setPageInfo(info);
            } else {
                PageInfo<EvalSummaryItemVO> info = statisticsService.getUserSummaryItemListByPage(query);
                Integer userTotal = statisticsService.getUserSummaryTotal(query);
                summaryVO.setPageInfo(info);
                summaryVO.setUserTotal(userTotal);
            }
        }else{
            summaryVO = new EvalSummaryVO();
        }
        summaryVO.setOrganizationTotal(query.getOrganizationIds().length);
        return ResultMessage.success(summaryVO);
    }

    /**
     * 估价汇总表导出
     *
     * @param query 查询条件
     * @return excel文件
     */
    @ApiOperation(value = "估价汇总表导出")
    @GetMapping("/summary/export")
    public ResponseEntity evaluationSummaryExport(@Valid @ModelAttribute EvalSummaryQuery query) {
        if (ArrayUtils.isEmpty(query.getOrganizationIds())) {
            return ResponseEntity.noContent().build();
        }
        String statMethod = String.valueOf(query.getStatMethod());
        if("2".equals(query.getSource()) || "3".equals(query.getSource())){
            return ResponseEntity.noContent().build();
        }
        String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp();
        if (SUMMARY_METHOD_ORG.equals(statMethod)) {
            List<EvalSummaryItemVO> info = statisticsService.getOrgSummaryItemList(query);
            if (CollectionUtils.isNotEmpty(info)) {
                //将VO转换为Excel导出对象
                AtomicInteger order = new AtomicInteger(1);
                List<EvalSummaryOrgItem> orgItemList = info.stream().map(item -> {
                    EvalSummaryOrgItem orgItem = new EvalSummaryOrgItem();
                    BeanUtils.copyProperties(item, orgItem);
                    orgItem.setOrderNo(order.getAndAdd(1));
                    return orgItem;
                }).collect(Collectors.toList());
                return ExportUtil.getExportExcelResponse(tmpPath, SUMMARY_METHOD_ORG_FILENAME,
                        EvalSummaryOrgItem.class, orgItemList);
            }
        } else {
            List<EvalSummaryItemVO> info = statisticsService.getUserSummaryItemList(query);
            if (CollectionUtils.isNotEmpty(info)) {
                //将VO转换为Excel导出对象
                AtomicInteger order = new AtomicInteger(1);
                List<EvalSummaryUserItem> userItemList = info.stream().map(item -> {
                    EvalSummaryUserItem userItem = new EvalSummaryUserItem();
                    BeanUtils.copyProperties(item, userItem);
                    String user = item.getLoginUser() + "(" + item.getUserName() + ")";
                    userItem.setUser(user);
                    userItem.setOrderNo(order.getAndAdd(1));
                    return userItem;
                }).collect(Collectors.toList());
                return ExportUtil.getExportExcelResponse(tmpPath, SUMMARY_METHOD_USER_FILENAME,
                        EvalSummaryUserItem.class, userItemList);
            }
        }
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "估价明细表")
    @GetMapping("/items")
    public ResultMessage evaluationItems(@Valid @ModelAttribute EvalItemsQuery query) {
        if (ArrayUtils.isEmpty(query.getOrganizationIds())) {
            return ResultMessage.success();
        }
        String statMethod = String.valueOf(query.getStatMethod());
        if("2".equals(query.getSource()) || "3".equals(query.getSource())){
            EvalItemsVO itemsVO = new EvalItemsVO();
            itemsVO.setOrganizationTotal(query.getOrganizationIds().length);
            return ResultMessage.success(itemsVO);
        }
        if("0".equals(query.getStatSubMethod())){
            query.setStatSubMethod(null);
        }
        if (Objects.nonNull(query.getStatSubMethod())) {
            String statSubMethod = query.getStatSubMethod();
            if (ITEMS_METHOD_EVAL.equals(statMethod)) {
                if (!StringUtils.equalsAny(statSubMethod, ITEMS_METHOD_EVAL_RAPID, ITEMS_METHOD_EVAL_BATCH)) {
                    return ResultMessage.paramError("statSubMethod");
                }
            } else {
                if (!StringUtils.equalsAny(statSubMethod, ITEMS_METHOD_REPORT_INQUIRY, ITEMS_METHOD_REPORT_ADVISORY)) {
                    return ResultMessage.paramError("statSubMethod");
                }
            }
        }
        EvalItemsVO itemsVO = statisticsService.getEvalItemsInfo(query);
        if (Objects.isNull(itemsVO)) {
            itemsVO = new EvalItemsVO();
        }
        itemsVO.setOrganizationTotal(query.getOrganizationIds().length);
        return ResultMessage.success(itemsVO);
    }

    /**
     * 估价明细表导出
     *
     * @param query 查询条件
     * @return excel文件
     */
    @ApiOperation(value = "估价明细表导出")
    @GetMapping("/items/export")
    public ResponseEntity evaluationItemsExport(@Valid @ModelAttribute EvalItemsQuery query) {
        if (ArrayUtils.isEmpty(query.getOrganizationIds())) {
            return ResponseEntity.noContent().build();
        }
        String statMethod = String.valueOf(query.getStatMethod());
        if("2".equals(query.getSource()) || "3".equals(query.getSource())){
            return ResponseEntity.noContent().build();
        }
        if("0".equals(query.getStatSubMethod())){
            query.setStatSubMethod(null);
        }
        if (Objects.nonNull(query.getStatSubMethod())) {
            String statSubMethod = query.getStatSubMethod();
            if (ITEMS_METHOD_EVAL.equals(statMethod)) {
                if (!StringUtils.equalsAny(statSubMethod, ITEMS_METHOD_EVAL_RAPID, ITEMS_METHOD_EVAL_BATCH)) {
                    return ResponseEntity.badRequest().body("statSubMethod error");
                }
            } else {
                if (!StringUtils.equalsAny(statSubMethod, ITEMS_METHOD_REPORT_INQUIRY, ITEMS_METHOD_REPORT_ADVISORY)) {
                    return ResponseEntity.badRequest().body("statSubMethod error");
                }
            }
        }
        String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp();
        List<EvalItemVO> itemVOList = statisticsService.getEvalItemsInfoByExport(query);
        if (CollectionUtils.isNotEmpty(itemVOList)) {
            //按估价方式统计
            if (ITEMS_METHOD_EVAL.equals(statMethod)) {
                AtomicInteger order = new AtomicInteger(1);
                List<EvalStatisticsItem> itemList = itemVOList.stream().map(item -> {
                    EvalStatisticsItem statisticsItem = new EvalStatisticsItem();
                    BeanUtils.copyProperties(item, statisticsItem);
                    statisticsItem.setOrderNo(order.getAndAdd(1));
                    String user = item.getLoginUser() + "(" + item.getUserName() + ")";
                    statisticsItem.setUser(user);
                    String totalPriceStr, unitPriceStr;
                    if(EvalConstant.EVAL_TYPE_SELL.equals(item.getEvalType().toString())){
                        totalPriceStr = item.getTotalPrice() + "万元";
                        unitPriceStr = item.getUnitPrice() + "元/㎡";
                    }else{
                        totalPriceStr = item.getTotalPrice() + "元/月";
                        unitPriceStr = item.getUnitPrice() + "元/天/㎡";
                    }
                    statisticsItem.setTotalPrice(totalPriceStr);
                    statisticsItem.setUnitPrice(unitPriceStr);
                    return statisticsItem;
                }).collect(Collectors.toList());
                return ExportUtil.getExportExcelResponse(tmpPath, ITEMS_METHOD_FILENAME,
                        EvalStatisticsItem.class, itemList);
            } else {
                AtomicInteger order = new AtomicInteger(1);
                List<EvalStatisticsItemReport> itemList = itemVOList.stream().map(item -> {
                    EvalStatisticsItemReport statisticsItem = new EvalStatisticsItemReport();
                    BeanUtils.copyProperties(item, statisticsItem);
                    statisticsItem.setOrderNo(order.getAndAdd(1));
                    String user = item.getLoginUser() + "(" + item.getUserName() + ")";
                    statisticsItem.setUser(user);
                    String totalPriceStr, unitPriceStr;
                    if(EvalConstant.EVAL_TYPE_SELL.equals(item.getEvalType().toString())){
                        totalPriceStr = item.getTotalPrice() + "万元";
                        unitPriceStr = item.getUnitPrice() + "元/㎡";
                    }else{
                        totalPriceStr = item.getTotalPrice() + "元/月";
                        unitPriceStr = item.getUnitPrice() + "元/天/㎡";
                    }
                    statisticsItem.setTotalPrice(totalPriceStr);
                    statisticsItem.setUnitPrice(unitPriceStr);
                    return statisticsItem;
                }).collect(Collectors.toList());
                return ExportUtil.getExportExcelResponse(tmpPath, ITEMS_METHOD_FILENAME,
                        EvalStatisticsItemReport.class, itemList);
            }
        }
        return ResponseEntity.noContent().build();
    }

}
