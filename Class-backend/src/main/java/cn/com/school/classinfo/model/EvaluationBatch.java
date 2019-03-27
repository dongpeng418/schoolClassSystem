package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class EvaluationBatch {

    private Integer id;
    private Integer tenantId;
    private Integer companyId;
    private Integer organizationId;
    private String taskCode;
    private String taskName;
    private Integer taskState;
    private String filePath;
    private String originalFilename;
    private Integer evalTotal;
    private Integer evalSuccess;
    private Integer evalFail;
    private Integer evalError;
    private Double evalAmount;
    private Date submitTime;
    private Date finishTime;
    private String recvPhone;
    private Date createTime;
    private String createBy;
    private String createName;
    private Date updateTime;
    private String updateBy;
    private String updateName;

}
