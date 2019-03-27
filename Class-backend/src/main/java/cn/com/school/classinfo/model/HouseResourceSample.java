package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class HouseResourceSample {

    private Integer id;
    private String evalCode;
    private String hrId;
    private String haCode;
    private String haName;
    private String distCode;
    private String distName;
    private String streetCode;
    private String streetName;
    private String streetNo;
    private String propTypeCode;
    private String propTypeName;
    private Double unitPrice;
    private String unitPriceUnit;
    private Double totalPrice;
    private String totalPriceUnit;
    private Double areaSize;
    private Integer floor;
    private Integer bheight;
    private String faceCode;
    private String faceName;
    private String intdecoCode;
    private String intdecoName;
    private Integer br;
    private Integer lr;
    private Integer cr;
    private Integer ba;
    private Date offerTime;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;

}
