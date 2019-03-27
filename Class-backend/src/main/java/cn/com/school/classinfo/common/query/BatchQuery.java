package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

/**
 * 批量估价查询条件
 * @author dongpp
 * @date 2018/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class BatchQuery extends BaseQuery{

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称", position = 1)
    private String taskName;

    /**
     * 任务状态，1：估价中，2：已完成，3：估价错误
     */
    @ApiModelProperty(value = "任务状态（1：估价中，2：已完成，3：估价错误）", position = 2)
    @Range(min = 1, max = 3, message = "evalType参数错误")
    private Integer taskState;

    @ApiModelProperty(hidden = true)
    private Integer id;

    @ApiModelProperty(hidden = true)
    private String taskCode;

}
