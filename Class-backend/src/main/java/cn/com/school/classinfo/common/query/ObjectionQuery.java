package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

/**
 * 估价异议查询条件
 * @author dongpp
 * @date 2018/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class ObjectionQuery extends BaseQuery{

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市", position = 1)
    private String city;

    /**
     * 小区
     */
    @ApiModelProperty(value = "小区", position = 2)
    private String ha;

    /**
     * 处理状态
     */
    @ApiModelProperty(value = "处理状态（1：待处理，2：处理完成）", position = 3)
    @Range(min = 1, max = 2, message = "state 参数错误")
    private Integer state;

    /**
     * 处理状态
     */
    @ApiModelProperty(hidden = true)
    private Integer id;

}
