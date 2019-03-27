/**
 *
 */
package cn.com.school.classinfo.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.model.SysOrganization;
import cn.com.school.classinfo.service.AuthCommonService;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.vo.SysOrganizationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dongpp
 */
@Api(tags = "机构相关接口")
@Validated
@RestController
@RequestMapping("/api/auth/org")
public class SysOrganizationController {

    private final AuthCommonService authCommonService;

    @Autowired
    public SysOrganizationController(AuthCommonService authCommonService) {
        this.authCommonService = authCommonService;
    }

    /**
     * 添加机构接口
     *
     * @param sysOrganization 机构信息
     * @return 失败与成功
     */
    @ApiOperation(value = "添加机构接口")
    @PostMapping("/add")
    public ResultMessage addOrgInfo(@Valid @RequestBody SysOrganization sysOrganization, HttpServletRequest request) {

        String name = sysOrganization.getName();
        if (Strings.isNullOrEmpty(name)) {
            return ResultMessage.requestError("机构信息【机构名称】不能为空!");
        }

        if (Objects.isNull(sysOrganization.getParentId())) {
            return ResultMessage.requestError("机构信息【parentId】不能为空!");
        }

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(sysOrganization, CreateUpdateEnum.CREATE, accessIp);

        //设置机构层级
        if(sysOrganization.getParentId() == 0){
            sysOrganization.setLevel(1);
        }else{
            SysOrganization parent = authCommonService.findOrgInfoById(sysOrganization.getParentId());
            if(Objects.isNull(parent)){
                return ResultMessage.requestError("机构信息【parentId】不存在!");
            }
            sysOrganization.setLevel(parent.getLevel() + 1);
        }

        //设置机构排序
        Integer priority = authCommonService.findOrgPriority(sysOrganization.getParentId());
        int currentPriority = Objects.isNull(priority) ? 1 : (priority + 1);
        sysOrganization.setPriority(currentPriority);
        int result = authCommonService.insertOrgInfo(sysOrganization);
        if (result > 0) {
            return ResultMessage.success();
        } else {
            return ResultMessage.requestError("机构信息添加失败!");
        }

    }

    /**
     * 修改机构接口
     *
     * @param sysOrganization 机构信息
     * @return 失败与成功
     */
    @ApiOperation(value = "修改机构接口")
    @PostMapping("/edit")
    public ResultMessage updateOrgInfo(@Valid @RequestBody SysOrganization sysOrganization, HttpServletRequest request) {

        if (Strings.isNullOrEmpty(sysOrganization.getName())) {
            return ResultMessage.requestError("机构信息【机构名称】不能为空!");
        }

        if (Objects.isNull(sysOrganization.getId())) {
            return ResultMessage.requestError("机构信息【ID】不能为空!");
        }

        SysOrganization existOrgInfo = authCommonService.findOrgInfoById(sysOrganization.getId());
        if (existOrgInfo == null) {
            return ResultMessage.requestError("要更新的机构不存在，ID = " + sysOrganization.getId());
        }

        SysOrganization newOrg = new SysOrganization();
        newOrg.setId(sysOrganization.getId());
        newOrg.setName(sysOrganization.getName());

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        CommonUtil.doCreateUpdateInfo(newOrg, CreateUpdateEnum.UPDATE, accessIp);
        int result = authCommonService.updateOrgInfo(newOrg);
        if (result > 0) {
            return ResultMessage.success();
        } else {
            return ResultMessage.requestError("机构信息更新失败!");
        }

    }

    /**
     * 更改机构层级结构
     *
     * @param orgId    机构ID
     * @param parentId 父ID
     * @return 失败与成功
     */
    @ApiOperation(value = "更改机构层级结构")
    @PostMapping("/changePid")
    public ResultMessage changeOrgParentId(@RequestParam Integer orgId,
                    @RequestParam Integer parentId,
                    HttpServletRequest request) {

        if (Objects.isNull(orgId)) {
            return ResultMessage.requestError("参数无效：orgId 为 空");
        }

        if (Objects.isNull(parentId)) {
            return ResultMessage.requestError("参数无效：parentId 为 空");
        }

        SysOrganization existOrgInfo = authCommonService.findOrgInfoById(orgId);
        if (existOrgInfo == null) {
            return ResultMessage.requestError("要更新的机构不存在：" + orgId);
        }

        String accessIp = HttpRequestUtil.getRemoteIp(request);
        SysOrganization sysOrganization = new SysOrganization();
        sysOrganization.setId(orgId);
        sysOrganization.setParentId(parentId);

        //设置机构层级
        if(sysOrganization.getParentId() == 0){
            sysOrganization.setLevel(1);
        }else{
            SysOrganization parent = authCommonService.findOrgInfoById(sysOrganization.getParentId());
            sysOrganization.setLevel(parent.getLevel() + 1);
        }

        //设置机构排序
        Integer priority = authCommonService.findOrgPriority(sysOrganization.getParentId());
        int currentPriority = Objects.isNull(priority) ? 1 : (priority + 1);
        sysOrganization.setPriority(currentPriority);
        CommonUtil.doCreateUpdateInfo(sysOrganization, CreateUpdateEnum.UPDATE, accessIp);
        int result = authCommonService.updateOrgInfo(sysOrganization);
        if (result > 0) {
            return ResultMessage.success();
        } else {
            return ResultMessage.requestError("机构信息更新失败!");
        }

    }

    /**
     * 查询登录用户管辖所有机构
     *
     * @return 失败与成功
     * 更改机构层级结构
     */
    @ApiOperation(value = "查询登录用户管辖所有机构")
    @GetMapping("/findMOrgs")
    public ResultMessage findUserManagedOrgs() {
        List<SysOrganizationVO> orgLists = authCommonService.findUserManagedOrgs();
        return ResultMessage.success(orgLists);
    }

    /**
     * 删除机构接口
     *
     * @param orgId  机构ID
     * @param status 机构状态
     * @return 失败与成功
     * 删除机构
     */
    @ApiOperation(value = "机构删除接口  逻辑删除 status 0：启用 1：停用 3：删除")
    @PostMapping("/delete")
    public ResultMessage deleteOrgInfo(@RequestParam Integer orgId,
                    HttpServletRequest request) {
        SysOrganization sysOrganization = new SysOrganization();
        String accessIp = HttpRequestUtil.getRemoteIp(request);
        sysOrganization.setId(orgId);
        sysOrganization.setStatus(3);
        CommonUtil.doCreateUpdateInfo(sysOrganization, CreateUpdateEnum.UPDATE, accessIp);
        boolean success = authCommonService.changeOrgInfoStatus(sysOrganization);
        if (success) {
            return ResultMessage.success();
        } else {
            return ResultMessage.requestError("机构信息删除失败!");
        }
    }

}
