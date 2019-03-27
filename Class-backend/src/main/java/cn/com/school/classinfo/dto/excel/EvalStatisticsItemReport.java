package cn.com.school.classinfo.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @author dongpp
 * @date 2018/11/24
 */
@Data
public class EvalStatisticsItemReport {

    @Excel(name = "序号")
    private Integer orderNo;

    /**
     * 机构名称
     */
    @Excel(name = "机构名称")
    private String organizationName;

    /**
     * 用户名称
     */
    @Excel(name = "帐号")
    private String user;

    /**
     * 日期
     */
    @Excel(name = "生成时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date evalDate;

    /**
     * 类型
     */
    @Excel(name = "生成报告", replace = {"询价报告_1", "咨询报告_2"})
    private String evalMethod;

    @Excel(name = "估价类型", replace = {"售价_1", "租金_2"})
    private Integer evalType;

    /**
     * 城市名称
     */
    @Excel(name = "城市")
    private String cityName;

    /**
     * 小区名称
     */
    @Excel(name = "小区名称")
    private String haName;

    /**
     * 地址
     */
    @Excel(name = "房产位置")
    private String location;

    /**
     * 面积
     */
    @Excel(name = "房产面积(㎡)")
    private Double bldgArea;

    /**
     * 总价万元
     */
    @Excel(name = "总价")
    private String totalPrice;

    /**
     * 单价元
     */
    @Excel(name = "单价")
    private String unitPrice;

}
