package cn.com.school.classinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 系统权限管理_资源表
 */
@Data
@ApiModel("资源信息")
public class SysResource implements Serializable{

    private static final long serialVersionUID = 5064281732496440705L;

    private Integer id;
    @ApiModelProperty("资源名称")
    private String name;
    @ApiModelProperty("资源级别")
    private Integer level;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("父ID")
    private Integer parentId;
    @ApiModelProperty("资源URL")
    private String url;
    @ApiModelProperty("资源类型，0代表菜单资源 1代表业务资源")
    private int resourceCategory;
    @ApiModelProperty(hidden = true)
    private int resourceType;
    @ApiModelProperty("状态  0：有效资源  1：无效资源")
    private Integer status;
    @JsonIgnore
    private Date createdTime;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String createdIp;
    @JsonIgnore
    private String updatedBy;
    @JsonIgnore
    private Date updatedTime;
    @JsonIgnore
    private String updatedIp;
    /**
     * 权限表code
     */
    @ApiModelProperty(hidden = true)
    private String code;

}
