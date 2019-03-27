package cn.com.school.classinfo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 区域VO
 * @author dongpp
 * @date 2018/10/24
 */
@Data
@ApiModel("区域对象")
public class RegionVO {
    @JsonIgnore
    @ApiModelProperty("ID")
    private Integer id;

    @JsonIgnore
    @ApiModelProperty("区域唯一码")
    private String regionKey;

    @ApiModelProperty("拼音首字母")
    private String pyKey;

    @ApiModelProperty("区域编码")
    private String regionCode;

    @ApiModelProperty("区域名称")
    private String regionName;

    @JsonIgnore
    @ApiModelProperty("父区域唯一码")
    private String parentKey;

    @JsonIgnore
    @ApiModelProperty("层级")
    private Integer level;

    @JsonIgnore
    @ApiModelProperty("GPS经度")
    private Double lon;

    @JsonIgnore
    @ApiModelProperty("GPS纬度")
    private Double lat;

    @ApiModelProperty("子区域列表")
    private List<RegionVO> items = Lists.newArrayList();
}
