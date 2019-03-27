package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 客户类别查询条件
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerCompanyQuery extends BaseQuery {

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "客户类型ID")
    private String categoryId;

    @ApiModelProperty(value = "省份编码")
    private String provinceCode;

    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    @ApiModelProperty(value = "联系人")
    private String contact;

}
