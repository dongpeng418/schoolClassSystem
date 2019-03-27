package cn.com.school.classinfo.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysAssetType {

    private Integer id;
    private Integer tenantId;
    private Integer companyId;
    private Integer organizationId;
    private String assetName;
    private String assetRemark;
    private Integer del;
    private Date createTime;
    private String createBy;
    private String createName;
    private Date updateTime;
    private String updateBy;
    private String updateName;
}
