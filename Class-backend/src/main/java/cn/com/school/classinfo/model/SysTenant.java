/**
 *
 */
package cn.com.school.classinfo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 渠道表 bean对象
 */
@Data
public class SysTenant implements Serializable{
    private static final long serialVersionUID = -8323676830468908142L;

    private int id;
    private String name;
    private String telephone;
    private String email;
    private int status;
    private String remark;
    private String domain;
    @ApiModelProperty(hidden = true)
    private Date createdTime;
    @ApiModelProperty(hidden = true)
    private String createdBy;
    @ApiModelProperty(hidden = true)
    private String createdIp;
    @ApiModelProperty(hidden = true)
    private String updatedBy;
    @ApiModelProperty(hidden = true)
    private Date updatedTime;
    @ApiModelProperty(hidden = true)
    private String updatedIp;

}
