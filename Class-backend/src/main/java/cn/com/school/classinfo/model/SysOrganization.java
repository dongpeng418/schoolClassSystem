package cn.com.school.classinfo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 权限  机构表
 *
 */
@Data
@ApiModel("机构信息")
public class SysOrganization implements Serializable{

    private static final long serialVersionUID = -2525083080824685830L;

    private Integer id;
    @ApiModelProperty(hidden = true)
    private Integer tenantId;
    @ApiModelProperty("机构名称")
    @NotBlank(message = "name 不能为空")
    private String name;
    @ApiModelProperty("父ID")
    @NotNull(message = "parentId 不能为空")
    private Integer parentId;
    @ApiModelProperty("级别")
    private Integer level;
    @ApiModelProperty("优先级")
    private Integer priority;
    @ApiModelProperty("状态，0：启用，1：停用")
    private Integer status;
    @ApiModelProperty("备注")
    private String remark;
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
