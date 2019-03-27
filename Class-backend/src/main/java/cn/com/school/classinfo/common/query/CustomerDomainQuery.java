package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户域名查询条件
 *
 * @author dongpp
 * @date 2018/12/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerDomainQuery extends BaseQuery {

    @ApiModelProperty(hidden = true)
    private String domain;

    @ApiModelProperty(hidden = true)
    private Integer companyId;

}
