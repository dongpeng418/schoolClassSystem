package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.EvalConstant;
import cn.com.school.classinfo.common.query.TenantEvalItemsQuery;
import cn.com.school.classinfo.common.query.TenantEvalSummaryQuery;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.dto.excel.TenantEvalStatisticsItem;
import cn.com.school.classinfo.dto.excel.TenantEvalStatisticsItemReport;
import cn.com.school.classinfo.dto.excel.TenantEvalSummaryItem;
import cn.com.school.classinfo.service.TenantStatisticsService;
import cn.com.school.classinfo.utils.ExportUtil;
import cn.com.school.classinfo.vo.TenantEvalItemVO;
import cn.com.school.classinfo.vo.TenantEvalItemsVO;
import cn.com.school.classinfo.vo.TenantEvalSummaryItemVO;
import cn.com.school.classinfo.vo.TenantEvalSummaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_EVAL;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.ITEMS_METHOD_FILENAME;
import static cn.com.school.classinfo.common.constant.StatisticsConstant.SUMMARY_METHOD_FILENAME;

/**
 * 估价统计接口
 *
 * @author dongpp
 * @date 2018/11/20
 */
@Api(tags = "渠道-查询统计接口")
@Validated
@RestController
@RequestMapping("/api/tenant/statistics")
public class TenantStatisticsController {

    private final AvsConfig avsConfig;

    private final TenantStatisticsService statisticsService;

    public TenantStatisticsController(AvsConfig avsConfig,
                                      TenantStatisticsService statisticsService) {
        this.avsConfig = avsConfig;
        this.statisticsService = statisticsService;
    }

    @ApiOperation(value = "估价汇总表")
    @GetMapping("/summary")
    public ResultMessage evaluationSummary(@Valid @ModelAttribute TenantEvalSummaryQuery query) {
        if("2".equals(query.getSource()) || "3".equals(query.getSource())){
            return ResultMessage.success();
        }
        TenantEvalSummaryVO summaryVO = statisticsService.getSummaryVO(query);
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
    public ResponseEntity evaluationSummaryExport(@Valid @ModelAttribute TenantEvalSummaryQuery query) {
        if("2".equals(query.getSource()) || "3".equals(query.getSource())){
            return ResponseEntity.noContent().build();
        }
        List<TenantEvalSummaryItemVO> info = statisticsService.getSummaryItemVO(query);
        String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp();
        if (CollectionUtils.isNotEmpty(info)) {
            //将VO转换为Excel导出对象
            AtomicInteger order = new AtomicInteger(1);
            List<TenantEvalSummaryItem> orgItemList = info.stream().map(item -> {
                TenantEvalSummaryItem orgItem = new TenantEvalSummaryItem();
                BeanUtils.copyProperties(item, orgItem);
                orgItem.setOrderNo(order.getAndAdd(1));
                return orgItem;
            }).collect(Collectors.toList());
            return ExportUtil.getExportExcelResponse(tmpPath, SUMMARY_METHOD_FILENAME,
                    TenantEvalSummaryItem.class, orgItemList);
        }
        return ResponseEntity.ok(ResultMessage.requestError("no data"));
    }

    @ApiOperation(value = "估价明细表")
    @GetMapping("/items")
    public ResultMessage evaluationItems(@Valid @ModelAttribute TenantEvalItemsQuery query) {
        if("2".equals(query.getSource()) || "3".equals(query.getSource())){
            return ResultMessage.success();
        }
        if("0".equals(query.getStatSubMethod())){
            query.setStatSubMethod(null);
        }
        TenantEvalItemsVO itemsVO = statisticsService.getItemsVO(query);
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
    public ResponseEntity evaluationItemsExport(@ModelAttribute TenantEvalItemsQuery query) {
        if("2".equals(query.getSource()) || "3".equals(query.getSource())){
            return ResponseEntity.noContent().build();
        }
        if("0".equals(query.getStatSubMethod())){
            query.setStatSubMethod(null);
        }
        List<TenantEvalItemVO> info = statisticsService.getEvalItemsInfoByExport(query);
        String tmpPath = avsConfig.getPath().getBase() + avsConfig.getPath().getTemp();
        String statMethod = String.valueOf(query.getStatMethod());
        if (CollectionUtils.isNotEmpty(info)) {
            //按估价方式统计
            if (ITEMS_METHOD_EVAL.equals(statMethod)) {
                AtomicInteger order = new AtomicInteger(1);
                List<TenantEvalStatisticsItem> itemList = info.stream().map(item -> {
                    TenantEvalStatisticsItem statisticsItem = new TenantEvalStatisticsItem();
                    BeanUtils.copyProperties(item, statisticsItem);
                    statisticsItem.setOrderNo(order.getAndAdd(1));
                    String user = item.getLoginUser() + "(" + item.getUserName() + ")";
                    statisticsItem.setUser(user);
                    String totalPriceStr, unitPriceStr;
                    if(EvalConstant.EVAL_TYPE_SELL.equals(item.getEvalType())){
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
                        TenantEvalStatisticsItem.class, itemList);
            } else {
                AtomicInteger order = new AtomicInteger(1);
                List<TenantEvalStatisticsItemReport> itemList = info.stream().map(item -> {
                    TenantEvalStatisticsItemReport statisticsItem = new TenantEvalStatisticsItemReport();
                    BeanUtils.copyProperties(item, statisticsItem);
                    statisticsItem.setOrderNo(order.getAndAdd(1));
                    String user = item.getLoginUser() + "(" + item.getUserName() + ")";
                    statisticsItem.setUser(user);
                    String totalPriceStr, unitPriceStr;
                    if(EvalConstant.EVAL_TYPE_SELL.equals(item.getEvalType())){
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
                        TenantEvalStatisticsItemReport.class, itemList);
            }
        }
        return ResponseEntity.ok(ResultMessage.requestError("no data"));
    }

}
