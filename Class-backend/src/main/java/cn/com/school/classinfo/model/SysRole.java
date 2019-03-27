/**
 *
 */
package cn.com.school.classinfo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dongpp
 * 系统管线管理  角色表
 *
 */
@Data
@ApiModel("角色信息")
public class SysRole implements Serializable{
    private static final long serialVersionUID = 5387938802086713285L;

    private Integer id;
    @ApiModelProperty(hidden = true)
    private Integer tenantId;
    @ApiModelProperty(hidden = true)
    private Integer organizationId;
    @ApiModelProperty(hidden = true)
    private String code;
    @ApiModelProperty("角色名称")
    @NotBlank(message = "name 不能为空")
    private String name;
    @ApiModelProperty("角色状态，0：启用，1：停用")
    @Range(min = 0, max = 1, message = "status 参数错误")
    private Integer status;
    @ApiModelProperty("客户公司ID")
    private Integer customerCompanyId;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty(hidden = true)
    private String roleType;
    @ApiModelProperty(hidden = true)
    private Date createdTime;
    @ApiModelProperty(hidden = true)
    private String createdBy;
    @ApiModelProperty(hidden = true)
    private String createdName;
    @ApiModelProperty(hidden = true)
    private String createdIp;
    @ApiModelProperty(hidden = true)
    private String updatedBy;
    @ApiModelProperty(hidden = true)
    private String updatedName;
    @ApiModelProperty(hidden = true)
    private Date updatedTime;
    @ApiModelProperty(hidden = true)
    private String updatedIp;

    /**
     * 角色下所拥有的权限列表
     */
    @ApiModelProperty("权限列表")
    private List<Integer> permissions;

}
