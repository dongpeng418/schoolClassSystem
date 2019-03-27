package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author dongpp
 * @date 2018/12/4
 */
@Data
@ApiModel
public class PageableQuery {
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", example = "1", position = 11)
    @Min(value = 1, message = "最小页码为1")
    protected Integer pageIndex = 1;

    /**
     * 页数
     */
    @ApiModelProperty(value = "每页数量", example = "10", position = 12)
    @Min(value = 1, message = "最小页数为1")
    protected Integer pageSize = 10;

    @ApiModelProperty(hidden = true)
    protected Integer pageStart;
}
