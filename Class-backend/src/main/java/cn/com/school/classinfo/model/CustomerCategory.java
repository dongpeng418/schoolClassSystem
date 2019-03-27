package cn.com.school.classinfo.model;

import cn.com.school.classinfo.annotation.Modify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel("客户类别")
public class CustomerCategory {

    private Integer id;

    @ApiModelProperty(hidden = true)
    private Integer tenantId;

    @NotBlank
    @ApiModelProperty(value = "类别名称", required = true)
    @Modify("客户类别名称")
    private String name;

    @ApiModelProperty(value = "类别备注")
    @Modify("客户类别备注")
    private String remark;

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