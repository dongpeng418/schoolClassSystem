/**
 *
 */
package cn.com.school.classinfo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 系统权限管理，用户_角色关联关系表
 *
 */
@Data
@ApiModel("用户角色信息")
public class SysUserRole implements Serializable{
    private static final long serialVersionUID = 5673913254267226658L;

    private Integer id;
    @ApiModelProperty("渠道ID")
    private Integer tenantId;
    @ApiModelProperty("机构ID")
    private Integer organizationId;
    @ApiModelProperty("用户ID")
    private Integer userId;
    @ApiModelProperty("角色ID")
    private Integer roleId;
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
