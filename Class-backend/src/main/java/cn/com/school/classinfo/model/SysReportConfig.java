package cn.com.school.classinfo.model;

import cn.com.school.classinfo.annotation.Modify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel("报告配置")
public class SysReportConfig {

    private Integer id;

    @ApiModelProperty(hidden = true)
    private Integer tenantId;

    @ApiModelProperty(value = "报告名称")
    @Modify("报告名称")
    @Length(max = 20, message = "报告名称 长度超过限制")
    private String reportName;

    @ApiModelProperty(value = "报告类型，1：询价，2：咨询", required = true)
    @NotNull(message = "reportType 不能为空")
    private Integer reportType;

    @ApiModelProperty(value = "评估声明")
    @Modify("评估声明")
    @Length(max = 2000, message = "评估声明 长度超过限制")
    private String reportStatement;

    @ApiModelProperty(value = "报告签名")
    @Modify("报告签名")
    @Length(max = 50, message = "报告签名 长度超过限制")
    private String reportSign;

    @ApiModelProperty(hidden = true)
    @Modify(value = "营业执照", type = Modify.Type.IMAGE)
    private String businessLicense;

    @ApiModelProperty(hidden = true)
    @Modify(value = "资质证书", type = Modify.Type.IMAGE)
    private String certificate;

    @ApiModelProperty(hidden = true)
    @Modify(value = "估价师签章1", type = Modify.Type.IMAGE)
    private String appraiserSign1;

    @ApiModelProperty(hidden = true)
    @Modify(value = "估价师签章2", type = Modify.Type.IMAGE)
    private String appraiserSign2;

    @ApiModelProperty(hidden = true)
    @Modify(value = "报告签章", type = Modify.Type.IMAGE)
    private String reportSignImg;

    @ApiModelProperty(hidden = true)
    private Integer isNew;

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