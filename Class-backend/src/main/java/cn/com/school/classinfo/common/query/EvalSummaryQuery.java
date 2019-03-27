package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

/**
 * 估价汇总统计查询条件
 * @author dongpp
 * @date 2018/11/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class EvalSummaryQuery extends BaseQuery {

    @ApiModelProperty(value = "需要统计的机构ID", required = true)
    private Integer[] organizationIds;

    @ApiModelProperty(value = "统计方法（1：按机构，2：按帐号）", required = true, position = 1)
    @Range(min = 1, max = 2, message = "statMethod 参数错误")
    private Integer statMethod;

    @ApiModelProperty(value = "帐号", position = 2)
    private String account;

    @ApiModelProperty(value = "来源", position = 3)
    private String source;
}
