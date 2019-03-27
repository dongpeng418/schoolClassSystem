package cn.com.school.classinfo.model;

import cn.com.school.classinfo.annotation.Modify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class SysEvaluationField {

    @ApiModelProperty(value = "ID", position = 1)
    private Integer id;

    @ApiModelProperty(hidden = true)
    private Integer tenantId;

    @ApiModelProperty(hidden = true)
    private Integer organizationId;

    @ApiModelProperty(hidden = true)
    private Integer companyId;

    @ApiModelProperty(hidden = true)
    private Integer type;

    @ApiModelProperty(hidden = true)
    private String fieldCode;

    @ApiModelProperty(hidden = true)
    private String fieldName;

    @ApiModelProperty(value = "是否填写")
    @NotNull(message = "display 不能为空")
    @Modify(value = "是否填写", type = Modify.Type.CHECKBOX)
    private Integer display;

    @ApiModelProperty(value = "是否必录")
    @NotNull(message = "required 不能为空")
    @Modify(value = "是否必录", type = Modify.Type.CHECKBOX)
    private Integer required;

    @ApiModelProperty(hidden = true)
    private String fieldType;

    @ApiModelProperty(hidden = true)
    private Integer entryType;

    @ApiModelProperty(hidden = true)
    private Integer orderNum;

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
