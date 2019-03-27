package cn.com.school.classinfo.model;

import cn.com.school.classinfo.annotation.Modify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel("客户公司")
public class CustomerCompany {

    private Integer id;

    @ApiModelProperty(hidden = true)
    private Integer tenantId;

    @ApiModelProperty(value = "公司名称", required = true)
    @NotBlank(message = "name 不能为空")
    @Modify("公司名称")
    @Length(max = 50, message = "公司名称 长度超过限制")
    private String name;

    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    @ApiModelProperty("省名称")
    @Modify("省")
    private String provinceName;

    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    @ApiModelProperty(value = "城市名称")
    @Modify("城市")
    private String cityName;

    @ApiModelProperty(value = "地址")
    @Modify("地址")
    @Length(max = 100, message = "公司地址 长度超过限制")
    private String address;

    @ApiModelProperty(value = "客户类别ID")
    @Modify("客户类别")
    private Integer categoryId;

    @ApiModelProperty(value = "联系人")
    @Modify("联系人")
    @Length(max = 20, message = "联系人 长度超过限制")
    private String contact;

    @ApiModelProperty(value = "社会统一码")
    @Modify("社会统一码")
    @Length(max = 50, message = "社会统一码 长度超过限制")
    private String socialCode;

    @ApiModelProperty(value = "营业执照", hidden = true)
    @Modify(value = "营业执照", type = Modify.Type.IMAGE)
    private String businessLicense;

    @ApiModelProperty(value = "联系电话")
    @Modify("联系电话")
    @Length(max = 100, message = "联系电话 长度超过限制")
    private String contactNumber;

    @ApiModelProperty(value = "E-mail")
    @Modify("E-mail")
    private String email;

    @ApiModelProperty(value = "传真")
    @Modify("传真")
    @Length(max = 100, message = "传真 长度超过限制")
    private String fax;

    @ApiModelProperty(value = "部门")
    @Modify("部门")
    @Length(max = 50, message = "部门 长度超过限制")
    private String department;

    @ApiModelProperty(value = "职位")
    @Modify("职位")
    @Length(max = 50, message = "职位 长度超过限制")
    private String position;

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