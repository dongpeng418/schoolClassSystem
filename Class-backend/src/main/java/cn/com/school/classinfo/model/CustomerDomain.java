package cn.com.school.classinfo.model;

import cn.com.school.classinfo.annotation.Modify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel("客户域名")
public class CustomerDomain {

    private Integer id;

    @ApiModelProperty(hidden = true)
    private Integer tenantId;

    @ApiModelProperty(value = "公司ID", required = true)
    @NotNull(message = "companyId 不能为空")
    @Modify("公司ID")
    private Integer companyId;

    @ApiModelProperty(hidden = true)
    private Integer categoryId;

    @ApiModelProperty(value = "域名Url")
    @NotBlank(message = "domain 不能为空")
    @Modify("域名Url")
    private String domain;

    @ApiModelProperty(hidden = true)
    private String serverIp;

    @ApiModelProperty(value = "处理状态：1、处理中，2：处理完成，3：处理失败", hidden = true)
    private Integer state;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private String createBy;

    @ApiModelProperty(hidden = true)
    private String createName;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private String updateBy;

    @ApiModelProperty(hidden = true)
    private String updateName;
}