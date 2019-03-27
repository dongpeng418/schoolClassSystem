package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import cn.com.school.classinfo.common.query.CustomerCategoryQuery;
import cn.com.school.classinfo.model.CustomerCategory;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.service.CustomerCategoryService;
import cn.com.school.classinfo.service.SysLogService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.utils.ObjectUtil;
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
import java.util.List;
import java.util.Objects;

/**
 * 客户类别
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Slf4j
@Validated
@Api(tags = "渠道-客户类别接口")
@RestController
@RequestMapping("/api/tenant/customer-category")
public class CustomerCategoryController {

    private final CustomerCategoryService customerCategoryService;

    private final SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    public CustomerCategoryController(CustomerCategoryService customerCategoryService,
                                      SysLogService sysLogService) {
        this.customerCategoryService = customerCategoryService;
        this.sysLogService = sysLogService;
    }

    /**
     * 增加客户类别
     *
     * @param customerCategory 客户类别
     * @return 响应消息
     */
    @ApiOperation(value = "增加客户类别")
    @PostMapping("/add")
    public ResultMessage add(@Valid @RequestBody CustomerCategory customerCategory) {

        customerCategoryService.add(customerCategory);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        sysLog.setOperateLog(String.format(SysLogConstant.CUSTOMER_CATEGORY_ADD, CommonUtil.getLoginUser().getLoginUser(),
                DateUtil.nowToString(), customerCategory.getName(), HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 更新客户类别
     *
     * @param customerCategory 客户类别
     * @return 响应消息
     */
    @ApiOperation(value = "更新客户类别")
    @PostMapping("/update")
    public ResultMessage update(@Valid @RequestBody CustomerCategory customerCategory) {
        if(Objects.isNull(customerCategory.getId())){
            return ResultMessage.paramError("id");
        }
        CustomerCategory category = customerCategoryService.getById(customerCategory.getId());
        customerCategoryService.update(customerCategory);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        String modifyDetail = ObjectUtil.getModifyInfo(customerCategory, category);
        sysLog.setOperateLog(String.format(SysLogConstant.CUSTOMER_CATEGORY_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                DateUtil.nowToString(), modifyDetail, HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 删除客户类别
     *
     * @param id 客户类别ID
     * @return 响应消息
     */
    @ApiOperation(value = "删除客户类别")
    @PostMapping("/delete")
    public ResultMessage delete(Integer id) {
        CustomerCategory category = customerCategoryService.getById(id);
        if(Objects.isNull(category)){
            return ResultMessage.paramError("id");
        }
        customerCategoryService.delete(id);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        sysLog.setOperateLog(String.format(SysLogConstant.CUSTOMER_CATEGORY_DELETE, CommonUtil.getLoginUser().getLoginUser(),
                DateUtil.nowToString(), category.getName(), HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 获取所有客户类别
     *
     * @return 响应消息
     */
    @ApiOperation(value = "获取所有客户类别列表")
    @GetMapping("/list")
    public ResultMessage list() {
        List<CustomerCategory> customerCategoryList = customerCategoryService.getListByQuery();
        return ResultMessage.success(customerCategoryList);
    }

    /**
     * 分页获取客户类别列表
     *
     * @return 响应消息
     */
    @ApiOperation(value = "获取客户类别列表-分页")
    @GetMapping("/pageable")
    public ResultMessage pageable(@Valid @ModelAttribute CustomerCategoryQuery query) {
        PageInfo<CustomerCategory> customerCategoryList = customerCategoryService.getPageableListByQuery(query);
        return ResultMessage.success(customerCategoryList);
    }



}
