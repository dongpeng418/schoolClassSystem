package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import cn.com.school.classinfo.common.query.CustomerDomainQuery;
import cn.com.school.classinfo.model.CustomerCompany;
import cn.com.school.classinfo.model.CustomerDomain;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.service.CustomerCompanyService;
import cn.com.school.classinfo.service.CustomerDomainService;
import cn.com.school.classinfo.service.SysLogService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.utils.ObjectUtil;
import cn.com.school.classinfo.vo.CustomerDomainVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * 客户域名
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Slf4j
@Validated
@Api(tags = "渠道-客户域名接口")
@RestController
@RequestMapping("/api/tenant/customer-domain")
public class CustomerDomainController {

    private final CustomerDomainService customerDomainService;

    private final CustomerCompanyService customerCompanyService;

    private final SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    public CustomerDomainController(CustomerDomainService customerDomainService,
                                    CustomerCompanyService customerCompanyService,
                                    SysLogService sysLogService) {
        this.customerDomainService = customerDomainService;
        this.customerCompanyService = customerCompanyService;
        this.sysLogService = sysLogService;
    }

    /**
     * 增加客户域名
     *
     * @param customerDomain 客户域名
     * @return 响应消息
     */
    @ApiOperation(value = "增加客户域名")
    @PostMapping("/add")
    public ResultMessage add(@Valid @RequestBody CustomerDomain customerDomain) {
        CustomerDomainQuery query = new CustomerDomainQuery();
        query.setCompanyId(customerDomain.getCompanyId());
        CustomerDomain domain = customerDomainService.getByQuery(query);
        if (Objects.nonNull(domain)) {
            return ResultMessage.duplicateError("该公司已增加域名");
        }
        query.setCompanyId(null);
        query.setDomain(customerDomain.getDomain());
        domain = customerDomainService.getByQuery(query);
        if (Objects.nonNull(domain)) {
            return ResultMessage.duplicateError("域名已存在");
        }
        CustomerCompany company = customerCompanyService.getById(customerDomain.getCompanyId());
        if(Objects.isNull(company)){
            return ResultMessage.paramError("companyId");
        }
        customerDomain.setCategoryId(company.getCategoryId());
        customerDomain.setState(1);
        customerDomainService.add(customerDomain);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        sysLog.setOperateLog(String.format(SysLogConstant.DOMAIN_ADD, CommonUtil.getLoginUser().getLoginUser(),
                DateUtil.nowToString(), customerDomain.getDomain(), HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 更新客户域名
     *
     * @param customerDomain 客户域名
     * @return 响应消息
     */
    @ApiOperation(value = "更新客户域名")
    @PostMapping("/update")
    public ResultMessage update(@Valid @RequestBody CustomerDomain customerDomain) {
        if(Objects.isNull(customerDomain.getId())){
            return ResultMessage.paramError("id");
        }
        CustomerDomain oldDomain = customerDomainService.getById(customerDomain.getId());
        CustomerDomainQuery query = new CustomerDomainQuery();
        query.setCompanyId(customerDomain.getCompanyId());
        CustomerDomain domain = customerDomainService.getByQuery(query);
        if (Objects.nonNull(domain) && !customerDomain.getId().equals(domain.getId())) {
            return ResultMessage.duplicateError("该公司已增加域名");
        }
        query.setCompanyId(null);
        query.setDomain(customerDomain.getDomain());
        domain = customerDomainService.getByQuery(query);
        if (Objects.nonNull(domain)&& !customerDomain.getId().equals(domain.getId())) {
            return ResultMessage.duplicateError("域名已存在");
        }
        CustomerCompany company = customerCompanyService.getById(customerDomain.getCompanyId());
        if(Objects.isNull(company)){
            return ResultMessage.paramError("companyId");
        }
        customerDomain.setCategoryId(company.getCategoryId());
        customerDomain.setState(1);
        customerDomainService.update(customerDomain);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        String modifyDetail = ObjectUtil.getModifyInfo(customerDomain, oldDomain);
        sysLog.setOperateLog(String.format(SysLogConstant.DOMAIN_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                DateUtil.nowToString(), modifyDetail, HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 删除客户域名
     *
     * @param id 客户域名ID
     * @return 响应消息
     */
    @ApiOperation(value = "删除客户域名")
    @PostMapping("/delete")
    public ResultMessage delete(Integer id) {
        CustomerDomain domain = customerDomainService.getById(id);
        if(Objects.isNull(domain)){
            return ResultMessage.paramError("id");
        }
        customerDomainService.delete(id);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        sysLog.setOperateLog(String.format(SysLogConstant.DOMAIN_DELETE, CommonUtil.getLoginUser().getLoginUser(),
                DateUtil.nowToString(), domain.getDomain(), HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 分页获取客户域名列表
     *
     * @return 响应消息
     */
    @ApiOperation(value = "获取客户域名列表-分页")
    @GetMapping("/pageable")
    public ResultMessage pageable(@Valid @ModelAttribute CustomerDomainQuery query) {
        PageInfo<CustomerDomainVO> customerDomainList = customerDomainService.getPageableListByQuery(query);
        return ResultMessage.success(customerDomainList);
    }


}
