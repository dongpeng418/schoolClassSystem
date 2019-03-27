package cn.com.school.classinfo.model;

import cn.com.school.classinfo.annotation.Modify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 权限管理  用户表
 */
@Data
@ApiModel("用户信息")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 4175243682320876413L;

    private Integer id;
    @ApiModelProperty(hidden = true)
    private Integer tenantId;
    @ApiModelProperty("用户名")
    @NotBlank(message = "userName 不能为空")
    @Modify("用户姓名")
    private String userName;
    @ApiModelProperty("联系方式")
    @Modify("联系方式")
    private String mobilePhone;
    @ApiModelProperty("E-mail")
    @Modify("E-mail")
    private String email;
    @ApiModelProperty("登录名")
    //@NotBlank(message = "loginUser 不能为空")
    @Modify("帐号")
    private String loginUser;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty(hidden = true)
    private String salt;
    @ApiModelProperty("用户状态，0：启用，1：停用")
    private Integer status;
    @ApiModelProperty("机构ID")
    private Integer organizationId;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty(value="客户公司ID",hidden = true)
    @Modify("客户公司ID")
    private Integer customerCompanyId;
    @ApiModelProperty(hidden = true)
    private Integer userType;
    @ApiModelProperty(hidden = true)
    private Date createdTime;
    @ApiModelProperty(hidden = true)
    private String createdBy;
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
    @ApiModelProperty(hidden = true)
    private Date lastLoginTime;
    @ApiModelProperty(hidden = true)
    private String lastLoginIp;
    @ApiModelProperty(hidden = true)
    private Date currentLoginTime;
    @ApiModelProperty(hidden = true)
    private String currentLoginIp;

}
