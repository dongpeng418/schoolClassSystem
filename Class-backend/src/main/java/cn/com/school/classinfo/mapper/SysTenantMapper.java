/**
 *
 */
package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysTenant;

/**
 * @author dongpp
 *
 */
public interface SysTenantMapper {
    SysTenantMapper selectBySelective(SysTenantMapper sysTenantMapper);

    SysTenant selectByDomain(SysTenant sysTenant);
}
