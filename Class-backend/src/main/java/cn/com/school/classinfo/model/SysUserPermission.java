package cn.com.school.classinfo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 系统权限管理  用户_权限 关联关系表  bean
 */
@Data
@ApiModel("用户权限信息")
public class SysUserPermission implements Serializable{
    private static final long serialVersionUID = 8719621230119876996L;

    private int id;
    @ApiModelProperty("渠道ID")
    private int tenantId;
    @ApiModelProperty("机构ID")
    private int organizationId;
    @ApiModelProperty("用户ID")
    private int userId;
    @ApiModelProperty("权限ID")
    private int permissionId;
    @ApiModelProperty(hidden = true)
    private Date createdTime;
    @ApiModelProperty(hidden = true)
    private String createdBy;
    @ApiModelProperty(hidden = true)
    private String createdIp;
    @ApiModelProperty(hidden = true)
    private String updatedBy;
    @ApiModelProperty(hidden = true)
    private Date updatedTime;
    @ApiModelProperty(hidden = true)
    private String updatedIp;

}
