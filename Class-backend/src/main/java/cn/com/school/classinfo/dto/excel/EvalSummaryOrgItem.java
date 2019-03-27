package cn.com.school.classinfo.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 估价汇总机构导出
 *
 * @author dongpp
 * @date 2018/11/23
 */
@Data
public class EvalSummaryOrgItem {

    @Excel(name = "序号")
    private Integer orderNo;

    /**
     * 机构名称
     */
    @Excel(name = "机构名称")
    private String organizationName;

    /**
     * 小区数
     */
    @Excel(name = "估价房产数")
    private Integer ha;

    /**
     * 快速估价数
     */
    @Excel(name = "快速估价（次）")
    private Integer rapid;

    /**
     * 批量估价数
     */
    @Excel(name = "批量估价（条）")
    private Integer batch;

    /**
     * 咨询报告数
     */
    @Excel(name = "咨询报告（份）")
    private Integer advisory;

    /**
     * 询价报告数
     */
    @Excel(name = "询价报告（份）")
    private Integer inquiry;


}
