package cn.com.school.classinfo.common.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产管理查询条件
 * @author dongpp
 * @date 2018/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("资产管理查询条件")
public class AssetQuery extends BaseQuery{
    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 小区
     */
    @ApiModelProperty(value = "小区", position = 1)
    private String ha;

    /**
     * 类型
     */
    @ApiModelProperty(value = "资产类型（查看字典：propType）", position = 2)
    private String type;

    /**
     * ID
     */
    @ApiModelProperty(hidden = true)
    private Integer id;


}
