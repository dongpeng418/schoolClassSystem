package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * @author dongpp
 * @date 2018/12/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class UserQuery extends PageableQuery {

    @ApiModelProperty(value = "机构ID列表", required = true)
    private List<Integer> organizationIds;

    @ApiModelProperty("登录名")
    private String loginUser;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("客户公司名")
    private String customerCompanyName;

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("渠道ID")
    private Integer tenantId;

    @ApiModelProperty("用户类型ID")
    private Integer userType;

    @ApiModelProperty("状态：0、启用，1、停用")
    @Range(min = 1, max = 2, message = "status 参数错误")
    private Integer status;
}
