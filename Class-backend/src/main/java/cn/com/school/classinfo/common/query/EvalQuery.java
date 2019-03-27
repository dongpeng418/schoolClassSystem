package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Pattern;

/**
 * 估价记录查询条件
 * @author dongpp
 * @date 2018/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("查询条件")
public class EvalQuery extends BaseQuery{

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 小区
     */
    @ApiModelProperty(value = "小区")
    private String ha;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String location;

    /**
     * 类型
     */
    @ApiModelProperty(value = "报告类型（1：询价，2：咨询）")
    @Pattern(regexp = "[1,2]", message = "type 参数错误")
    private String type;

    /**
     * 评估类型
     */
    @ApiModelProperty(value = "评估类型（1：售价，2：租金）")
    @Pattern(regexp = "[1,2]", message = "evalType 参数错误")
    private String evalType;

    /**
     * 估价唯一码
     */
    @ApiModelProperty(hidden = true)
    private String evalCode;

    /**
     * 估价ID
     */
    @ApiModelProperty(hidden = true)
    private Integer evalRecordId;

    /**
     * 估价历史ID
     */
    @ApiModelProperty(hidden = true)
    private Integer evalHistoryId;

}
