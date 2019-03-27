package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class Region {

    private Integer id;
    private String regionKey;
    private String regionCode;
    private String regionName;
    private String parentKey;
    private Integer level;
    private Double lon;
    private Double lat;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;

}
