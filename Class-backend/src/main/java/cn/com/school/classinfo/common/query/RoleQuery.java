package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dongpp
 * @date 2018/12/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class RoleQuery extends BaseQuery {

    @ApiModelProperty("ID")
    private Integer code;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("更新人")
    private String updateName;

    @ApiModelProperty("角色类型")
    private String roleType;

    @ApiModelProperty("机构类型")
    private Integer organizationId;

    @ApiModelProperty("客户公司类型")
    private Integer customerCompanyId;

}
