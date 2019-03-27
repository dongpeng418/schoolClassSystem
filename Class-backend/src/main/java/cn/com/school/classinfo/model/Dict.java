package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class Dict {

    private Integer id;
    private String dictCode;
    private String dictName;
    private String dictTypeCode;
    private String dictTypeName;
    private Integer valid;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;

}
