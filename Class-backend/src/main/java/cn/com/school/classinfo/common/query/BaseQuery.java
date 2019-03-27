package cn.com.school.classinfo.common.query;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.model.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 基础查询条件
 * @author dongpp
 * @date 2018/10/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class BaseQuery extends PageableQuery{

    /**
     * 查询起始时间
     */
    @ApiModelProperty(value = "查询起始时间（yyyy/MM/dd）", position = 13)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    protected Date beginDate;

    /**
     * 查询结束时间
     */
    @ApiModelProperty(value = "查询结束时间（yyyy/MM/dd）", position = 14)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    protected Date endDate;

    /**
     * 根据登录用户查询
     */
    @ApiModelProperty(hidden = true)
    protected String loginUser;

    /**
     * 渠道ID
     */
    @ApiModelProperty(hidden = true)
    protected Integer tenantId;

    /**
     * 客户公司ID
     */
    @ApiModelProperty(hidden = true)
    protected Integer companyId;

    /**
     * 设置查询认证信息
     */
    public void authInfo(){
        SysUser sysUser = CommonUtil.getLoginUser();
        this.setTenantId(sysUser.getTenantId());
        this.setCompanyId(sysUser.getCustomerCompanyId());
        this.setLoginUser(sysUser.getLoginUser());
    }
}
