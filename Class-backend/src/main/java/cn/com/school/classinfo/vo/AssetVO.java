package cn.com.school.classinfo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 资产管理VO
 * @author dongpp
 * @date 2018/10/29
 */
@Data
public class AssetVO {
    private Integer id;
    private Integer evalId;
    private String assetType;
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
    private Integer historyCount;
    private Integer businessTypeId;
}