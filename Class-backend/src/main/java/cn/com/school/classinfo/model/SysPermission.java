/**
 *
 */
package cn.com.school.classinfo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 系统权限管理  权限表
 *
 */
@Data
public class SysPermission implements Serializable{
    private static final long serialVersionUID = 3792118163491728510L;

    private int id;
    @ApiModelProperty(hidden = true)
    private int tenantId;
    @ApiModelProperty(hidden = true)
    private int organizationId;
    @ApiModelProperty(hidden = true)
    private String name;
    @ApiModelProperty(hidden = true)
    private int status;
    @ApiModelProperty(hidden = true)
    private String remark;
    @ApiModelProperty(hidden = true)
    private int resourceId;
    @ApiModelProperty(hidden = true)
    private String code;
    @ApiModelProperty(hidden = true)
    private String permType;
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
