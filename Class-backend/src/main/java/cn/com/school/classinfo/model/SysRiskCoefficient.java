package cn.com.school.classinfo.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel
public class SysRiskCoefficient {

    @ApiModelProperty(value = "ID", position = 1)
    private Integer id;

    @ApiModelProperty(value = "渠道ID", hidden = true)
    private Integer tenantId;

    @ApiModelProperty(value = "公司ID", hidden = true)
    private Integer companyId;

    @ApiModelProperty(value = "机构ID", hidden = true)
    private Integer organizationId;

    @ApiModelProperty(value = "省编码", position = 2)
    @NotBlank(message = "provinceCode 不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "省名称", position = 3)
    @NotBlank(message = "provinceName 不能为空")
    private String provinceName;

    @ApiModelProperty(value = "城市编码", position = 4)
    private String cityCode;

    @ApiModelProperty(value = "城市名称", position = 5)
    private String cityName;

    @ApiModelProperty(value = "区县编码", position = 6)
    private String distCode;

    @ApiModelProperty(value = "区县名称", position = 7)
    private String distName;

    @ApiModelProperty(value = "业务ID", position = 8)
    private String businessId;

    @ApiModelProperty(value = "业务名称", position = 9)
    private String businessName;

    @ApiModelProperty(value = "价格系数", position = 10)
    @Range(min = 0, max = 1, message = "priceCoefficient 区间为[0-1]")
    private Double priceCoefficient;

    @ApiModelProperty(hidden = true)
    private Integer del;

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
