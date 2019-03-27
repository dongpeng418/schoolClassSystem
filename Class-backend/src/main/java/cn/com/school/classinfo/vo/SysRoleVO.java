/**
 *
 */
package cn.com.school.classinfo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dongpp
 * 系统管线管理  角色表
 *
 */
@Data
@ApiModel("角色信息")
public class SysRoleVO implements Serializable{
    private static final long serialVersionUID = 5387938802086713285L;

    private int id;
    private int tenantId;
    private int organizationId;
    private int customerCompanyId;
    private String name;
    private String code;
    private int status;
    private String remark;
    private Date createdTime;
    private String createdBy;
    private String createdName;
    private String createdIp;
    private String updatedBy;
    private String updatedName;
    private Date updatedTime;
    private String updatedIp;
    private Integer userCount;

}
