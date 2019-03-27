package cn.com.school.classinfo.common;

import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;


/**
 * @author dongpp
 */
@Slf4j
public class CommonUtil {

    /**
     * 增加创建信息（用户，时间，更新时间（可选），更新人（可选））
     */
    public static void doCreateUpdateInfo(Object obj, CreateUpdateEnum createUpdateEnum, String accessIp) {
        SysUser user = getLoginUser();
        Date now = DateUtil.now();
        if (createUpdateEnum.equals(CreateUpdateEnum.CREATE)) {
            Method createUidM = BeanUtils.findDeclaredMethod(obj.getClass(), "setCreatedBy", String.class);
            invoke(createUidM, obj, user.getLoginUser());
            Method createNameM = BeanUtils.findDeclaredMethod(obj.getClass(), "setCreatedName", String.class);
            invoke(createNameM, obj, user.getUserName());
            Method createTimeM = BeanUtils.findDeclaredMethod(obj.getClass(), "setCreatedTime", Date.class);
            invoke(createTimeM, obj, now);
            Method createdIpM = BeanUtils.findDeclaredMethod(obj.getClass(), "setCreatedIp", String.class);
            invoke(createdIpM, obj, accessIp);
            Method tenantId = BeanUtils.findDeclaredMethod(obj.getClass(), "setTenantId", Integer.class);
            invoke(tenantId, obj, user.getTenantId());
        }
        Method updateUidM = BeanUtils.findDeclaredMethod(obj.getClass(), "setUpdatedBy", String.class);
        invoke(updateUidM, obj, user.getLoginUser());
        Method updateNameM = BeanUtils.findDeclaredMethod(obj.getClass(), "setUpdatedName", String.class);
        invoke(updateNameM, obj, user.getUserName());
        Method updateTimeM = BeanUtils.findDeclaredMethod(obj.getClass(), "setUpdatedTime", Date.class);
        invoke(updateTimeM, obj, now);
        Method updatedIpM = BeanUtils.findDeclaredMethod(obj.getClass(), "setUpdatedIp", String.class);
        invoke(updatedIpM, obj, accessIp);
    }

    /**
     * 增加创建信息（用户登录名，用户姓名，更新登录名，更新用户名称，渠道ID，机构ID）
     */
    public static void doEvalCreateUpdateInfo(Object obj, CreateUpdateEnum createUpdateEnum) {
        Date now = DateUtil.now();
        SysUser user = getLoginUser();
        if (createUpdateEnum.equals(CreateUpdateEnum.CREATE)) {
            Method createTimeM = BeanUtils.findDeclaredMethod(obj.getClass(), "setCreateTime", Date.class);
            invoke(createTimeM, obj, now);
            Method createUidM = BeanUtils.findDeclaredMethod(obj.getClass(), "setCreateBy", String.class);
            invoke(createUidM, obj, user.getLoginUser());
            Method createUserName = BeanUtils.findDeclaredMethod(obj.getClass(), "setCreateName", String.class);
            invoke(createUserName, obj, user.getUserName());
            Method tenantId = BeanUtils.findDeclaredMethod(obj.getClass(), "setTenantId", Integer.class);
            invoke(tenantId, obj, user.getTenantId());
            Method organizationId = BeanUtils.findDeclaredMethod(obj.getClass(), "setOrganizationId", Integer.class);
            invoke(organizationId, obj, user.getOrganizationId());
            Method companyIdM = BeanUtils.findDeclaredMethod(obj.getClass(), "setCompanyId", Integer.class);
            invoke(companyIdM, obj, user.getCustomerCompanyId());
        }
        Method updateTimeM = BeanUtils.findDeclaredMethod(obj.getClass(), "setUpdateTime", Date.class);
        invoke(updateTimeM, obj, now);
        Method updateUidM = BeanUtils.findDeclaredMethod(obj.getClass(), "setUpdateBy", String.class);
        invoke(updateUidM, obj, user.getLoginUser());
        Method updateUserName = BeanUtils.findDeclaredMethod(obj.getClass(), "setUpdateName", String.class);
        invoke(updateUserName, obj, user.getUserName());
    }

    /**
     * 设置操作日志信息
     */
    public static void doOperateLogInfo(Object obj) {
        SysUser user = getLoginUser();
        doOperateLogInfo(obj, user);
    }

    /**
     * 设置操作日志信息
     */
    public static void doOperateLogInfo(Object obj, SysUser user) {
        Date now = DateUtil.now();
        Method updateTimeM = BeanUtils.findDeclaredMethod(obj.getClass(), "setOperateTime", Date.class);
        invoke(updateTimeM, obj, now);
        Method updateUidM = BeanUtils.findDeclaredMethod(obj.getClass(), "setOperateBy", String.class);
        invoke(updateUidM, obj, user.getLoginUser());
        Method updateUserName = BeanUtils.findDeclaredMethod(obj.getClass(), "setOperateName", String.class);
        invoke(updateUserName, obj, user.getUserName());
        Method tenantId = BeanUtils.findDeclaredMethod(obj.getClass(), "setTenantId", Integer.class);
        invoke(tenantId, obj, user.getTenantId());
    }

    /**
     * 方法调用
     *
     * @param method 方法
     * @param object 调用的对象
     * @param param  参数
     */
    private static void invoke(Method method, Object object, Object param) {
        if (Objects.isNull(method) || Objects.isNull(object)) {
            return;
        }
        try {
            method.invoke(object, param);
        } catch (Exception e) {
            log.warn("set user audit info error. msg: ", e);
        }
    }

    /**
     * 获取当前登录用户
     *
     * @return 登录用户
     */
    public static SysUser getLoginUser() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (Objects.isNull(user)) {
            throw new AuthenticationException("请登录");
        }
        return user;
    }

    /**
     * 获取当前登录用户的渠道ID
     *
     * @return 渠道ID
     */
    public static Integer getTenantId() {
        return getLoginUser().getTenantId();
    }

}
