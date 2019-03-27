package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class EvaluationRecordHistory {

    private Integer id;
    private Integer tenantId;
    private Integer companyId;
    private Integer organizationId;
    private Integer evalId;
    /**
     * 估价类型，1：售价，2：租金
     */
    private Integer evalType;
    private String evalCode;
    private String cityCode;
    private String cityName;
    private String distCode;
    private String distName;
    private String buildNo;
    private String unit;
    private String roomNo;
    private String propType;
    /**
     * 房屋用途名称
     */
    private String propTypeName;
    private Double bldgArea;
    private Integer br;
    private Integer lr;
    private Integer cr;
    private Integer ba;
    private Integer floor;
    private Integer bheight;
    private String faceCode;
    private String faceName;
    private String bldgTypeCode;
    private String bldgTypeName;
    private String buildYear;
    private String intdecoCode;
    private String intdecoName;
    private String indoStruCode;
    private String indoStruName;
    private String proprtCode;
    private String proprtName;
    private String location;
    private String haCode;
    private String haName;
    private Double gpsLon;
    private Double gpsLat;
    private String gpsType;
    private Double unitPrice;
    private Double totalPrice;
    private Date evalDate;
    private Integer inquiry;
    private Integer advisory;
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
