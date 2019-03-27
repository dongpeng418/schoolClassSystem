package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

/**
 * 风控系数查询条件
 *
 * @author dongpp
 * @date 2018/11/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class RiskQuery extends BaseQuery {

    @ApiModelProperty(value = "省编码", position = 1)
    private String provinceCode;

    @ApiModelProperty(value = "城市编码", position = 2)
    private String cityCode;

    @ApiModelProperty(value = "区县编码", position = 3)
    private String distCode;

    @ApiModelProperty(value = "业务名称", position = 4)
    private String businessName;

    @ApiModelProperty(value = "价格系数最小", position = 5)
    @Range(min = 0, max = 1, message = "coefficientMin 区间为[0-1]")
    private Double coefficientMin;

    @ApiModelProperty(value = "价格系数最大", position = 6)
    @Range(min = 0, max = 1, message = "coefficientMax 区间为[0-1]")
    private Double coefficientMax;
}
