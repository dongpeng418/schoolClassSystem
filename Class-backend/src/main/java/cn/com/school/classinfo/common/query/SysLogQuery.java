package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统日志查询条件
 *
 * @author dongpp
 * @date 2018/12/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SysLogQuery extends BaseQuery {

    @ApiModelProperty("账号")
    private String loginUser;

    @ApiModelProperty("姓名")
    private String userName;

}
