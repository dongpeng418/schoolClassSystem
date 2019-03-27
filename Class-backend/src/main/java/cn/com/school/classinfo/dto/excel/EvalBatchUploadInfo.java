package cn.com.school.classinfo.dto.excel;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 批量估价导入信息对象
 * @author dongpp
 */
@Data
public class EvalBatchUploadInfo {

    @Excel(name = "城市（必填）")
    private String cityName;

    @Excel(name = "行政区")
    private String distName;

    @Excel(name = "楼栋")
    private String buildNo;

    @Excel(name = "单元")
    private String unit;

    @Excel(name = "户")
    private String roomNo;

    @Excel(name = "用途（必填）[使用下拉选项]")
    private String propType;

    @Excel(name = "面积（必填）")
    private Double bldgArea;

    @Excel(name = "室")
    private Integer br;

    @Excel(name = "厅")
    private Integer lr;

    @Excel(name = "卫")
    private Integer cr;

    @Excel(name = "楼层")
    private Integer floor;

    @Excel(name = "楼高")
    private Integer bheight;

    @Excel(name = "朝向[使用下拉选项]")
    private String faceName;

    @Excel(name = "建筑类型[使用下拉选项]")
    private String bldgTypeName;

    @Excel(name = "建筑年代")
    private String buildYear;

    @Excel(name = "装修程度[使用下拉选项]")
    private String intdecoName;

    @Excel(name = "室内结构[使用下拉选项]")
    private String indoStruName;

    @Excel(name = "证载地址")
    private String location;

    @Excel(name = "小区名称（必填）")
    private String haName;

    @Override
    public String toString() {
        return "{" +
                "cityName='" + cityName + '\'' +
                ", distName='" + distName + '\'' +
                ", buildNo='" + buildNo + '\'' +
                ", unit='" + unit + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", propType='" + propType + '\'' +
                ", bldgArea=" + bldgArea +
                ", br=" + br +
                ", lr=" + lr +
                ", cr=" + cr +
                ", floor=" + floor +
                ", bheight=" + bheight +
                ", faceName='" + faceName + '\'' +
                ", bldgTypeName='" + bldgTypeName + '\'' +
                ", buildYear='" + buildYear + '\'' +
                ", intdecoName='" + intdecoName + '\'' +
                ", indoStruName='" + indoStruName + '\'' +
                ", location='" + location + '\'' +
                ", haName='" + haName + '\'' +
                '}';
    }
}
