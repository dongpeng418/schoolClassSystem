package cn.com.school.classinfo.vo;


import cn.com.school.classinfo.annotation.Modify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class SysWebsiteConfigVO {

    @ApiModelProperty(value = "ID")
    @NotNull(message = "id 不能为空")
    private Integer id;

    @ApiModelProperty(value = "渠道ID", hidden = true)
    private Integer tenantId;

    @ApiModelProperty(value = "机构ID", hidden = true)
    private Integer organizationId;

    @ApiModelProperty(value = "域名", hidden = true)
    private String domain;

    @ApiModelProperty(value = "主题颜色")
    @Modify("主题颜色")
    private String themeColor;

    @ApiModelProperty(value = "公司名称")
    @Modify("公司名称")
    @Length(max = 50, message = "公司名称 长度超过限制")
    private String companyName;

    @ApiModelProperty(value = "LOGO文件路径", hidden = true)
    @Modify(value = "LOGO", type = Modify.Type.IMAGE)
    private String logoPath;

    @ApiModelProperty(value = "联系方式")
    @Modify("联系方式")
    @Length(max = 50, message = "联系方式 长度超过限制")
    private String contact;

    @ApiModelProperty(value = "E-mail")
    @Modify("E-mail")
    private String email;

    @ApiModelProperty(value = "公司地址")
    @Modify("公司地址")
    @Length(max = 100, message = "公司地址 长度超过限制")
    private String address;

    @ApiModelProperty(value = "关于我们")
    @Modify("关于我们")
    @Length(max = 500, message = "关于我们 长度超过限制")
    private String companyProfile;

    @ApiModelProperty(value = "系统名称")
    @NotBlank(message = "productName 不能为空")
    @Length(max = 50, message = "系统名称 长度超过限制")
    @Modify("系统名称")
    private String productName;

    @ApiModelProperty(value = "背景图片文件路径", hidden = true)
    @Modify(value = "背景图片", type = Modify.Type.IMAGE)
    private String backgroundPath;

    @ApiModelProperty(hidden = true)
    private String updateBy;

    @ApiModelProperty(hidden = true)
    private String updateName;

}
