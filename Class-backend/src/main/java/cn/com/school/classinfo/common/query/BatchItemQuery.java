package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 批量估价查询条件
 * @author dongpp
 * @date 2018/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class BatchItemQuery extends BaseQuery {

    /**
     * 批量估价ID
     */
    @ApiModelProperty(value = "批量估价记录ID", required = true, position = 1)
    @NotNull(message = "batchId 不能为空")
    private Integer batchId;

    /**
     * 估价状态
     */
    @ApiModelProperty(value = "估价状态（0：未评估，1：评估成功，2：评估失败，3：解析失败）", position = 2)
    @Range(min = 0, max = 3, message = "evalState 参数错误")
    private Integer evalState;


}
