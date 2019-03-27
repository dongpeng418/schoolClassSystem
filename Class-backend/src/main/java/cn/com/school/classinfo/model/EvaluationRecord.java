package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class EvaluationRecord {

    /**
     * ID
     */
    private Integer id;

    private Integer tenantId;

    private Integer companyId;

    private Integer organizationId;

    /**
     * 估价类型，1：售价，2：租金
     */
    private Integer evalType;

    /**
     * 估价唯一码
     */
    private String evalCode;

    /**
     * 城市码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区县码
     */
    private String distCode;

    /**
     * 区县名称
     */
    private String distName;

    /**
     * 楼座号
     */
    private String buildNo;

    /**
     * 单元
     */
    private String unit;

    /**
     * 房间
     */
    private String roomNo;

    /**
     * 房屋用途码
     */
    private String propType;

    /**
     * 房屋用途名称
     */
    private String propTypeName;

    /**
     * 面积
     */
    private Double bldgArea;

    /**
     * 室
     */
    private Integer br;

    /**
     * 厅
     */
    private Integer lr;

    /**
     * 卫
     */
    private Integer cr;

    /**
     * 厨
     */
    private Integer ba;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 楼高
     */
    private Integer bheight;

    /**
     * 朝向码
     */
    private String faceCode;

    /**
     * 朝向名称
     */
    private String faceName;

    /**
     * 建筑类型码
     */
    private String bldgTypeCode;

    /**
     * 建筑类型名称
     */
    private String bldgTypeName;

    /**
     * 建筑年代
     */
    private String buildYear;

    /**
     * 装修程度码
     */
    private String intdecoCode;

    /**
     * 装修程度名称
     */
    private String intdecoName;

    /**
     * 室内结构码
     */
    private String indoStruCode;

    /**
     * 室内结构名称
     */
    private String indoStruName;

    /**
     * 房屋权属码
     */
    private String proprtCode;

    /**
     * 房屋权属名称
     */
    private String proprtName;

    /**
     * 地址
     */
    private String location;

    /**
     * 小区码
     */
    private String haCode;

    /**
     * 小区名称
     */
    private String haName;

    /**
     * 经度
     */
    private Double gpsLon;

    /**
     * 纬度
     */
    private Double gpsLat;

    /**
     * GPS类型
     */
    private String gpsType;

    /**
     * 单价元
     */
    private Double unitPrice;

    /**
     * 总价万元
     */
    private Double totalPrice;

    /**
     * 评估日期
     */
    private Date evalDate;

    /**
     * 是否询价1：是，0：否
     */
    private Integer inquiry;

    /**
     * 是否咨询，1：是，0：否
     */
    private Integer advisory;

    /**
     * 估价历史次数
     */
    private Integer historyCount;

    /**
     * 是否加入资产管理，1：是，0：否
     */
    private Integer asset;

    /**
     * 样本最高单价
     */
    private Double samplePriceMax;

    /**
     * 样本最低单价
     */
    private Double samplePriceMin;

    /**
     * 样本平均单价
     */
    private Double samplePriceAvg;

    /**
     * 样本数量
     */
    private Integer sampleCount;

    /**
     * 委托人
     */
    private String principal;

    /**
     * 所有权人
     */
    private String owner;

    /**
     * 证载地址
     */
    private String cardAddress;

    /**
     * 业务类型ID
     */
    private Integer businessTypeId;

    private Date createTime;
    private String createBy;
    private String createName;
    private Date updateTime;
    private String updateBy;
    private String updateName;

}
