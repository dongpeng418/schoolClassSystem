package cn.com.school.classinfo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 权限  机构表VO
 *
 * @author dongpp
 */
@Data
public class SysOrganizationVO{
    private Integer id;
    private Integer tenantId;
    private String name;
    private Integer parentId;
    private Integer level;
    private Integer priority;
    private Integer status;
    private String remark;
    private Date createdTime;
    private String createdBy;
    private String createdIp;
    private String updatedBy;
    private Date updatedTime;
    private String updatedIp;
    /**
     * 子项
     */
    private List<SysOrganizationVO> children;
}
