/**
 *
 */
package cn.com.school.classinfo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 系统权限  操作表
 *
 */
@Data
@ApiModel("系统权限操作信息")
public class SysOperation implements Serializable{
    private static final long serialVersionUID = -1721814805357568829L;

    private int id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("操作类型")
    private int operType;
    @ApiModelProperty("值")
    private String value;
    private Date createdTime;
    private String createdBy;
    private String createdIp;
    private String updatedBy;
    private Date updatedTime;
    private String updatedIp;

}
