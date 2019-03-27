package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class EvaluationBatchItem {

    private Integer id;
    private Integer tenantId;
    private Integer companyId;
    private Integer organizationId;
    private Integer batchId;
    private Integer evalState;
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
    private Date createTime;
    private String createBy;
    private String createName;
    private Date updateTime;
    private String updateBy;
    private String updateName;

}
