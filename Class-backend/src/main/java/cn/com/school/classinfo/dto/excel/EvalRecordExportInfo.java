package cn.com.school.classinfo.dto.excel;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 估价记录导出信息对象
 * @author dongpp
 */
@Data
public class EvalRecordExportInfo {

    @Excel(name = "序号")
    private Integer orderNo;

    @Excel(name = "估价时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date evalDate;

    @Excel(name = "城市")
    private String cityName;

    @Excel(name = "小区名称")
    private String haName;

    @Excel(name = "房产位置")
    private String location;

    @Excel(name = "房产面积", suffix = "m²")
    private Double bldgArea;

    @Excel(name = "估价类型", replace = {"售价_1", "租金_2"})
    private Integer evalType;

    @Excel(name = "估价结果")
    private String totalPrice;

    @Excel(name = "报告类型")
    private String reportType;

    @Excel(name = "估价历史", suffix = "次")
    private Integer evalCount;

}
