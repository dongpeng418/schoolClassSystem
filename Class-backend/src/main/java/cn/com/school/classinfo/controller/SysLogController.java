package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.query.SysLogQuery;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.service.SysLogService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 网站配置接口
 *
 * @author dongpp
 * @date 2018/11/21
 */
@Api(tags = "渠道-系统日志接口")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/tenant/logs")
public class SysLogController {

    private final SysLogService sysLogService;

    public SysLogController(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @ApiOperation(value = "日志列表")
    @GetMapping("/list")
    public ResultMessage get(@Valid @ModelAttribute SysLogQuery query) {
        PageInfo<SysLog> list = sysLogService.getListByQuery(query);
        return ResultMessage.success(list);
    }

}
