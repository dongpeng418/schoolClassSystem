package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 渠道-估价汇总统计查询条件
 * @author dongpp
 * @date 2018/11/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class TenantEvalSummaryQuery extends BaseQuery {

    @ApiModelProperty(value = "客户公司", position = 1)
    private String companyName;

    @ApiModelProperty(value = "客户帐号", position = 2)
    private String account;

    @ApiModelProperty(value = "客户类别", position = 3)
    private Integer categoryId;

    @ApiModelProperty(value = "来源", position = 4)
    private String source;
}
