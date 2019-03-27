package cn.com.school.classinfo.dto;

import cn.com.school.classinfo.validator.HouseValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 房屋信息
 * 
 * @author hanpt
 *
 */
@Data
@ApiModel("房产信息")
@HouseValidator
public class HouseInfo {

    @ApiModelProperty(value = "城市码")
    private String cityCode;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "区县码")
    private String distCode;

    @ApiModelProperty(value = "区县名称")
    private String distName;

    @ApiModelProperty(value = "楼座号")
    private String buildNo;

    @ApiModelProperty(value = "单元")
    private String unit;

    @ApiModelProperty(value = "房间")
    private String roomNo;

    @ApiModelProperty(value = "房屋用途码")
    private String propType;

    @ApiModelProperty(value = "面积")
    private Double bldgArea;

    @ApiModelProperty(value = "室")
    private Integer br;

    @ApiModelProperty(value = "厅")
    private Integer lr;

    @ApiModelProperty(value = "卫")
    private Integer cr;

    @ApiModelProperty(value = "厨")
    private Double ba;

    @ApiModelProperty(value = "楼层")
    private Integer floor;

    @ApiModelProperty(value = "楼高")
    private Integer bheight;

    @ApiModelProperty(value = "朝向码")
    private String face;

    @ApiModelProperty(value = "朝向名称")
    private String facename;

    @ApiModelProperty(value = "建筑类型码")
    private String bldgType;

    @ApiModelProperty(value = "建筑类型名称")
    private String bldgtypename;

    @ApiModelProperty(value = "建筑年代")
    private String buildYear;

    @ApiModelProperty(value = "装修程度码")
    private String intDeco;

    @ApiModelProperty(value = "装修程度名称")
    private String intdeconame;

    @ApiModelProperty(value = "室内结构码")
    private String indoStru;

    @ApiModelProperty(value = "室内结构名称")
    private String indoStruName;

    @ApiModelProperty(value = "房屋权属码")
    private String proprt;

    @ApiModelProperty(value = "房屋权属名称")
    private String proprtname;

    @ApiModelProperty(value = "地址")
    private String location;

    @ApiModelProperty(value = "小区信息")
    private HaSimpleInfo haInfo;

    @ApiModelProperty(value = "GPS信息")
    private GpsInfo gpsInfo;

    @ApiModelProperty(value = "房产联系人")
    private String suitcontact;

    @ApiModelProperty(value = "房产联系电话")
    private String suitphone;

    @ApiModelProperty(value = "邮储业务类型")
    private String postalServiceType;

    @ApiModelProperty(value = "系统地址")
    private String hamainstreet;

    @ApiModelProperty(value = "委托人")
    private String principal;

    @ApiModelProperty(value = "所有权人")
    private String owner;

    @ApiModelProperty(value = "证载地址")
    private String cardAddress;

    @ApiModelProperty(value = "业务类型ID")
    private Integer businessTypeId;
}
