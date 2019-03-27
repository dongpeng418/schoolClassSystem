package cn.com.school.classinfo.dto.excel;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 批量估价导入信息对象
 * @author dongpp
 */
@Data
public class BatchItemExportInfo {

    @Excel(name = "序号")
    private Integer orderNo;

    @Excel(name = "城市")
    private String cityName;

    @Excel(name = "行政区")
    private String distName;

    @Excel(name = "证载地址")
    private String location;

    @Excel(name = "小区名称")
    private String haName;

    @Excel(name = "房产用途")
    private String propType;

    @Excel(name = "面积")
    private Double bldgArea;

    @Excel(name = "楼层")
    private Integer floor;

    @Excel(name = "总楼层")
    private Integer bheight;

    @Excel(name = "评估单价", suffix = "元")
    private Double unitPrice;

    @Excel(name = "评估总价", suffix = "万元")
    private Double totalPrice;


}
