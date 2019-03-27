package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class EvaluationReport {

    private Integer id;
    private Integer tenantId;
    private Integer companyId;
    private Integer organizationId;
    private Integer reportConfigId;
    private String evalCode;
    private String reportCode;
    private String reportType;
    private String reportPath;
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
