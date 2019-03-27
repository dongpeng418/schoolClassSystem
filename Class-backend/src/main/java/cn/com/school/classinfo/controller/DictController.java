package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.model.Dict;
import cn.com.school.classinfo.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 字典接口
 *
 * @author dongpp
 * @date 2018/10/25
 */
@Api(tags = "字典接口", position = 1)
@RestController
@RequestMapping("/api/dict")
public class DictController {

    private final DictService dictService;

    @Autowired
    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @ApiOperation("字典列表")
    @GetMapping("/list")
    public ResultMessage list(@ApiParam(value = "字典类型码，如果没有则查询全部数据")
                              @RequestParam(required = false) String dictTypeCode) {
        Map<String, List<Dict>> dictMap = dictService.getDictMap(dictTypeCode);
        if (MapUtils.isEmpty(dictMap)) {
            return ResultMessage.paramError();
        }
        return ResultMessage.success(dictMap);
    }

}
