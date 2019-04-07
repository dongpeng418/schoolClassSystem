/**
 *
 */
package cn.com.school.classinfo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.model.ScSlnInfo;
import cn.com.school.classinfo.model.ScStudentSelect;
import cn.com.school.classinfo.model.ScUser;
import cn.com.school.classinfo.service.ScSlnInfoService;
import cn.com.school.classinfo.service.ScStudentSelectService;
import cn.com.school.classinfo.service.ScUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 禧泰_董鹏鹏
 *
 */
@Api(tags = "学生选班方案相关接口")
@Validated
@RestController
@RequestMapping("/api/scheme")
@Slf4j
public class ScStudentSchemeController {

    @Autowired
    private ScSlnInfoService scSlnInfoService;

    @Autowired
    private ScUserService scUserService;

    @Autowired
    private ScStudentSelectService scStudentSelectService;

    /**
     * @return 方案列表
     */
    @ApiOperation(value = "学生可选方案列表，0代表固定班，1代表走读;0和1都得选，并且只能选择一个")
    @GetMapping("/lists")
    public ResultMessage lists() {
        List<ScSlnInfo> results = scSlnInfoService.getAllDatas();
        if(results == null || results.size() ==0) {
            return ResultMessage.apiError("没有走班方案数据,请初始化方案列表");
        }
        return ResultMessage.success().data(results);
    }

    /**
     * @return 方案列表
     */
    @ApiOperation(value = "userId为路径参数，必填;body参数是ScSlnInfo的list，两条记录，一条type：0  一条type:1")
    @PostMapping("/student/selected/scheme/{userId}")
    public ResultMessage selectedScheme(@PathVariable("userId") Integer userId,@RequestBody List<ScSlnInfo> results) {

        ScUser scUser = scUserService.getUserById(userId);
        if(scUser == null) {
            return ResultMessage.apiError("该学生不存在");
        }
        if(results == null || results.size() ==0) {
            return ResultMessage.apiError("该学生没有选定方案");
        }
        if(results.size()>2) {
            return ResultMessage.apiError("固班和走读只能选一个");
        }
        boolean isSelectedTypeOne = false;
        boolean isSelectedTypeTwo = false;
        int type = 3;
        for (ScSlnInfo scSlnInfo : results) {
            type = scSlnInfo.getsType();
            if(type == 0) {
                isSelectedTypeOne = true;
            }
            if(type == 1) {
                isSelectedTypeTwo = true;
            }
        }

        if(isSelectedTypeOne&&isSelectedTypeTwo) {//代表两个都选了
            ScStudentSelect scStudentSelect = null;
            for (ScSlnInfo scSlnInfo : results) {
                scStudentSelect = new ScStudentSelect();
                scStudentSelect.setsId(scSlnInfo.getsId());
                scStudentSelect.setStuId(scUser.getUserId());
                scStudentSelectService.saveSelectedScheme(scStudentSelect);
            }
        }

        return ResultMessage.success();
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        List<ScSlnInfo> resluts = new ArrayList<ScSlnInfo>();
        ScSlnInfo scSlnInfo = new ScSlnInfo();
        scSlnInfo.setsId(2);
        scSlnInfo.setsType(0);
        resluts.add(scSlnInfo);

        ScSlnInfo scSlnInfo1 = new ScSlnInfo();
        scSlnInfo1.setsId(2);
        scSlnInfo1.setsType(0);
        resluts.add(scSlnInfo1);

        System.out.println(gson.toJson(resluts));
    }

}
