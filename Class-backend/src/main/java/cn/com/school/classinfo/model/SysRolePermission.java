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
 * 系统权限管理  角色_权限关联关系表 bean
 *
 */
@Data
public class SysRolePermission implements Serializable{
    private static final long serialVersionUID = -1320857004833990767L;

    private int id;
    @ApiModelProperty(hidden = true)
    private int tenantId;
    @ApiModelProperty("机构ID")
    private int organizationId;
    @ApiModelProperty("角色ID")
    private int roleId;
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
