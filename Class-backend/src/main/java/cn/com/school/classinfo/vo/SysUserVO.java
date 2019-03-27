package cn.com.school.classinfo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author dongpp
 * @date 2018/12/4
 */
@Data
public class SysUserVO {

    private Integer id;
    private Integer tenantId;
    private String userName;
    private String mobilePhone;
    private String email;
    private String loginUser;
    @JsonIgnore
    private String password;
    private String salt;
    private Integer status;
    private Integer organizationId;
    private String remark;
    private String organizationName;
    private String parentOrgName;
    private String roleNames;
    private String customerCompanyName;
    private Date createdTime;
    private String createdBy;
    private String createdIp;
    private String updatedBy;
    private String updatedName;
    private String updatedByName;
    private Date updatedTime;
    private String updatedIp;
    private Date lastLoginTime;
    private String lastLoginIp;
    private Date currentLoginTime;
    private String currentLoginIp;
    private Integer userType;
    private Integer customerCompanyId;
    private List<SysRoleSimpleVO> roleList;
}
