package cn.com.school.classinfo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDomainVO {

    private Integer id;

    private Integer tenantId;

    private Integer companyId;

    private String companyName;

    private Integer categoryId;

    private String categoryName;

    private String domain;

    private String serverIp;

    private Integer state;

    private Date createTime;

    private String createBy;

    private String createName;

    private Date updateTime;

    private String updateBy;

    private String updateName;
}