/**
 *
 */
package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysOrganization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dongpp
 * 系统权限管理_机构相关操作
 */
public interface SysOrganizationMapper {

    SysOrganization selectBySelective(SysOrganization sysOrganization);

    int insert(SysOrganization sysOrganization);

    int updateById(SysOrganization sysOrganization);

    Integer selectOrgPriority(Integer parentId);

    SysOrganization findOrgInfoById(@Param("id") int id);

    SysOrganization findOrgInfoByName(@Param("name") String name);

    int changeOrgParentId(@Param("orgId") int orgId,@Param("parentId") int parentId);

    /**
     * 递归得到子机构信息
     * @param id
     * @return
     */
    List<SysOrganization> findRecursionChildOrgInfos(@Param("id") int id);

}
