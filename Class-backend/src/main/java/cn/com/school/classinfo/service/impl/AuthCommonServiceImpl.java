/**
 *
 */
package cn.com.school.classinfo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.RoleQuery;
import cn.com.school.classinfo.common.query.UserQuery;
import cn.com.school.classinfo.mapper.SysOrganizationMapper;
import cn.com.school.classinfo.mapper.SysPermissionMapper;
import cn.com.school.classinfo.mapper.SysResourceMapper;
import cn.com.school.classinfo.mapper.SysRoleMapper;
import cn.com.school.classinfo.mapper.SysRolePermissionMapper;
import cn.com.school.classinfo.mapper.SysTenantMapper;
import cn.com.school.classinfo.mapper.SysUserMapper;
import cn.com.school.classinfo.mapper.SysUserRoleMapper;
import cn.com.school.classinfo.model.SysOrganization;
import cn.com.school.classinfo.model.SysPermission;
import cn.com.school.classinfo.model.SysResource;
import cn.com.school.classinfo.model.SysRole;
import cn.com.school.classinfo.model.SysRolePermission;
import cn.com.school.classinfo.model.SysTenant;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.model.SysUserRole;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.vo.SysOrganizationVO;
import cn.com.school.classinfo.vo.SysRoleSimpleVO;
import cn.com.school.classinfo.vo.SysRoleVO;
import cn.com.school.classinfo.vo.SysUserVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongpp
 *
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "REGION")
@Transactional(rollbackFor = Exception.class)
public class AuthCommonServiceImpl implements AuthCommonService{

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysTenantMapper sysTenantMapper;


    @Override
    public List<SysResource> selectNeedAuthcs() {
        return sysResourceMapper.selectNeedAuthcs();
    }

    @Override
    public List<SysResource> selectUserPermissions(String loginUser) {
        return sysResourceMapper.selectUserPermissions(loginUser);
    }

    @Override
    public List<SysResource> selectUserPermissionsByUserType(String loginUser,int userType) {
        return sysResourceMapper.selectUserPermissionsByUserType(loginUser,userType);
    }

    @Override
    public int insertSysUser(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

    @Override
    public SysUser findUserByLoginUser(String loginUser) {
        return sysUserMapper.findUserByLoginUser(loginUser);
    }

    @Override
    public SysUser findUserByLoginUser(String loginUser,int userType) {
        return sysUserMapper.findUserByLoginUserAndUserType(loginUser,userType);
    }

    @Override
    public SysUser findUserByLoginUserAndCompanyId(String loginUser,int customerCompanyId) {
        return sysUserMapper.findUserByLoginUserAndCId(loginUser,customerCompanyId);
    }

    @Override
    public List<SysUser> findUserByCIdAndUserType(int userType,int customerCompanyId) {
        return sysUserMapper.findUserByCIdAndUserType(userType,customerCompanyId);
    }

    @Override
    public SysUser findUserByLoginUserAndUTypeAndTId(String loginUser,int userType,int tenantId) {
        return sysUserMapper.findUserByLoginUserAndUTypeAndTId(loginUser,userType,tenantId);
    }

    @Override
    public SysUser findUserById(int id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public SysUser findAllStatusUserById(int id) {
        return sysUserMapper.selectAllStatusUserById(id);
    }

    @Override
    public int updateByLoginUserAndCId(SysUser sysUser) {
        return sysUserMapper.updateByLoginUserAndCId(sysUser);
    }

    @Override
    public int updateTenantSysUser(SysUser sysUser) {
        return sysUserMapper.updateByLoginUser(sysUser);
    }

    @Override
    public SysOrganization findOrgInfoByName(String name) {
        return sysOrganizationMapper.findOrgInfoByName(name);
    }

    @Override
    public int insertOrgInfo(SysOrganization sysOrganization) {
        return sysOrganizationMapper.insert(sysOrganization);
    }

    @Override
    public int updateOrgInfo(SysOrganization sysOrganization) {
        int count = sysOrganizationMapper.updateById(sysOrganization);
        return count;
    }

    @Override
    public SysOrganization findOrgInfoById(int id) {
        return sysOrganizationMapper.findOrgInfoById(id);
    }

    @Override
    public Integer findOrgPriority(Integer parentId) {
        return sysOrganizationMapper.selectOrgPriority(parentId);
    }

    @Override
    public List<SysOrganizationVO> findUserManagedOrgs() {
        SysUser userInfo = CommonUtil.getLoginUser();
        int orgId = userInfo.getOrganizationId();
        Subject subject = SecurityUtils.getSubject();
        //是否有本级机构查询权限
        if(!subject.isPermitted(new WildcardPermission("org:*"))){
            return null;
        }
        //是否有下级机构查询权限
        //如果没有，只查当前机构，如果有查下级机构
        List<SysOrganization> organizations;
        if(!subject.isPermitted(new WildcardPermission("org:*"))){
            organizations = Lists.newArrayList();
            SysOrganization organization = sysOrganizationMapper.findOrgInfoById(orgId);
            organizations.add(organization);
        }else{
            organizations = sysOrganizationMapper.findRecursionChildOrgInfos(orgId);
        }
        if(CollectionUtils.isNotEmpty(organizations)){
            Map<String, SysOrganizationVO> map = Maps.newHashMap();
            List<SysOrganizationVO> voList = Lists.newArrayList();
            organizations.forEach(org -> {
                SysOrganizationVO vo = new SysOrganizationVO();
                BeanUtils.copyProperties(org, vo);
                map.put(Integer.toString(org.getId()), vo);
            });
            organizations.forEach(org -> {
                SysOrganizationVO vo = map.get(String.valueOf(org.getId()));
                SysOrganizationVO parent = map.get(String.valueOf(org.getParentId()));
                if(Objects.isNull(parent)){
                    voList.add(vo);
                }else{
                    if(CollectionUtils.isNotEmpty(parent.getChildren())){
                        parent.getChildren().add(vo);
                    }else{
                        List<SysOrganizationVO> subList = Lists.newArrayList();
                        subList.add(vo);
                        parent.setChildren(subList);
                    }
                }
            });
            return voList;
        }
        return null;
    }

    @Override
    public boolean addUserRoleInfo(SysRole sysRole) {
        int result = sysRoleMapper.insert(sysRole);
        if(result == 0) {
            return false;
        }
        //插入角色后生成角色编码，规则：机构ID + 角色ID
        String orgId = Objects.isNull(sysRole.getOrganizationId()) ? "" : sysRole.getOrganizationId().toString();
        String roleCode = orgId + sysRole.getId();
        SysRole ur = new SysRole();
        ur.setId(sysRole.getId());
        ur.setCode(roleCode);
        sysRoleMapper.update(ur);

        //保存角色权限
        savePermission(sysRole);
        return true;
    }

    @Override
    public boolean editUserRoleInfo(SysRole sysRole) {
        int result = sysRoleMapper.update(sysRole);
        if(result == 0) {
            return false;
        }
        List<Integer> permis = sysRole.getPermissions();
        if(permis.size()>0) {
            //删除之前关联的权限,重新建立新的关联关系
            sysRolePermissionMapper.deleteByRoleId(sysRole.getId());
            savePermission(sysRole);
        }
        return true;
    }

    private void savePermission(SysRole sysRole){
        if(CollectionUtils.isEmpty(sysRole.getPermissions())){
            return;
        }
        SysRolePermission sysRolePermission;
        for (Integer permId : sysRole.getPermissions()) {
            sysRolePermission = new SysRolePermission();
            sysRolePermission.setPermissionId(permId);
            sysRolePermission.setRoleId(sysRole.getId());
            if(sysRole.getTenantId()!=null) {
                sysRolePermission.setTenantId(sysRole.getTenantId());
            }
            if(sysRole.getOrganizationId()!=null) {
                sysRolePermission.setOrganizationId(sysRole.getOrganizationId());
            }
            sysRolePermission.setCreatedBy(sysRole.getUpdatedBy());
            sysRolePermission.setCreatedIp(sysRole.getUpdatedIp());
            sysRolePermission.setCreatedTime(sysRole.getUpdatedTime());
            sysRolePermission.setUpdatedBy(sysRole.getUpdatedBy());
            sysRolePermission.setUpdatedIp(sysRole.getUpdatedIp());
            sysRolePermission.setUpdatedTime(sysRole.getUpdatedTime());
            sysRolePermissionMapper.insert(sysRolePermission);
        }
    }

    @Override
    public SysRole findRoleInfoByName(SysRole sysRole) {
        return sysRoleMapper.selectRoleInfoByName(sysRole);
    }

    @Override
    public SysRole findRoleInfoById(int id) {
        SysRole sysRole = new SysRole();
        sysRole.setId(id);
        return sysRoleMapper.selectBySelective(sysRole);
    }

    @Override
    public boolean deleteRoleInfoById(int id) {
        int result = sysRoleMapper.delete(id);
        if(result>0) {
            sysRolePermissionMapper.deleteByRoleId(id);
        }
        return true;
    }

    @Override
    public int updateSysUserStatusById(int id, int status) {
        return sysUserMapper.updateStatusById(id, status);
    }

    @Override
    public boolean distributionUserRole(HttpServletRequest request,int userId, List<Integer> roleIds) {
        //删除之前的角色信息
        sysUserRoleMapper.deleteByUserId(userId);
        //保存现在的
        for (Integer roleId : roleIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            String accessIp = HttpRequestUtil.getRemoteIp(request);
            CommonUtil.doCreateUpdateInfo(sysUserRole, CreateUpdateEnum.CREATE, accessIp);
            sysUserRole.setOrganizationId(CommonUtil.getLoginUser().getOrganizationId());
            sysUserRoleMapper.insert(sysUserRole);
        }
        return true;
    }

    @Override
    public boolean changeOrgInfoStatus(SysOrganization sysOrganization) {
        sysOrganizationMapper.updateById(sysOrganization);
        if(sysOrganization.getStatus() == 3) {//代表删除
            sysUserMapper.updateStatusByOrgId(3,sysOrganization.getId());
        }
        return true;
    }

    @Override
    public List<SysResource> selectNeedDistributeReses() {
        return sysResourceMapper.selectNeedDistributeReses();
    }

    @Override
    public List<SysResource> selectNeedDistributeReses(int resType) {
        return sysResourceMapper.selectNeedDistributeResesByResType(resType);
    }

    @Override
    public List<SysPermission> selectPermissionsByRoleId(int roleId) {
        return sysPermissionMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public PageInfo<SysRoleVO> findRoleList(RoleQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(sysRoleMapper.selectByQuery(query));
    }

    @Override
    public List<SysRoleSimpleVO> findAllRoleByCondition(RoleQuery query) {
        return sysRoleMapper.selectAllByCondition(query);
    }

    @Override
    public List<SysRoleSimpleVO> findRoleByUserId(Integer userId) {
        return sysRoleMapper.selectByUserId(userId);
    }

    @Override
    public PageInfo<SysUserVO> findUserList(UserQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(sysUserMapper.selectByQuery(query));
    }

    @Override
    public PageInfo<SysUserVO> findTenantUserList(UserQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(sysUserMapper.selectTenantUsersByQuery(query));
    }

    @Override
    public PageInfo<SysUserVO> findCustomerUserList(UserQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(sysUserMapper.selectCustomerUsersByQuery(query));
    }

    @Override
    public List<SysResource> selectCustomerUserCanPermissions(String loginUser, int resType) {
        return sysResourceMapper.selectCustomerUserCanPermissions(loginUser, resType);
    }

    @Override
    public PageInfo<SysRoleVO> findRoleListByTenantId(RoleQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(sysRoleMapper.findRoleListByTenantId(query));
    }

    @Override
    public List<SysRoleSimpleVO> findRoleListByTenantIdNoPage(RoleQuery query){
        return sysRoleMapper.findSimpleRoleListByTenantId(query);
    }

    @Override
    public SysTenant selectByDomain(SysTenant param) {
        return sysTenantMapper.selectByDomain(param);
    }
}
