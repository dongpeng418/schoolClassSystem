/**
 *
 */
package cn.com.school.classinfo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.school.classinfo.model.ScStudentInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 禧泰_董鹏鹏
 *
 */
@Api(tags = "官网相关接口")
@Validated
@RestController
@RequestMapping("/api")
@Slf4j
public class IndexController {

    @ApiOperation(value = "获取avm日志记录，count和list,参数pageSize")
    @GetMapping("/test")
    public ScStudentInfo avmLog(
    		@ApiParam(value="用户类别，1老师 2学生",required=true)
    		String type
    		) {
    	ScStudentInfo scStudentInfo = new ScStudentInfo();
    	scStudentInfo.setStuNo("001");
        return scStudentInfo;
    }
}
