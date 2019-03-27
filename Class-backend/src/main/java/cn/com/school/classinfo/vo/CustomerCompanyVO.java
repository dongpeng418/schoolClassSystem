package cn.com.school.classinfo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerCompanyVO {

    private Integer id;

    private Integer tenantId;

    private String name;

    private String provinceCode;

    private String provinceName;

    private String cityCode;

    private String cityName;

    private String address;

    private Integer categoryId;

    private String categoryName;

    private String contact;

    private String socialCode;

    private String businessLicense;

    private String contactNumber;

    private String email;

    private String fax;

    private String department;

    private String position;

    private Date createTime;

    private String createBy;

    private String createName;

    private Date updateTime;

    private String updateBy;

    private String updateName;
}