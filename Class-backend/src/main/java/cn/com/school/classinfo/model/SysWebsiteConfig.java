package cn.com.school.classinfo.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysWebsiteConfig {

    private Integer id;

    private Integer tenantId;

    private Integer organizationId;

    private Integer type;

    private String domain;

    private String themeColor;

    private String companyName;

    private String logoPath;

    private String contact;

    private String email;

    private String address;

    private String companyProfile;

    private String productName;

    private String backgroundPath;

    private Integer configType;

    private Date createTime;

    private String createBy;

    private String createName;

    private Date updateTime;

    private String updateBy;

    private String updateName;
}