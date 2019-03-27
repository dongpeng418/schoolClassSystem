package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

/**
 * 估价明细统计查询条件
 *
 * @author dongpp
 * @date 2018/11/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class EvalItemsQuery extends BaseQuery {

    @ApiModelProperty(value = "需要统计的机构ID", required = true)
    private Integer[] organizationIds;

    @ApiModelProperty(value = "统计方法（1：估价方式，2：报告方式）", required = true)
    @Range(min = 1, max = 2, message = "statMethod 参数错误")
    private Integer statMethod;

    @ApiModelProperty(value = "统计子方法（11：快速估价，12：批量估价，21：询价报告，22：咨询报告）", position = 1)
    @Pattern(regexp = "0|11|12|21|22", message = "statSubMethod 参数错误")
    private String statSubMethod;

    @ApiModelProperty(value = "帐号", position = 2)
    private String account;

    @ApiModelProperty(value = "来源", position = 3)
    private String source;
}
