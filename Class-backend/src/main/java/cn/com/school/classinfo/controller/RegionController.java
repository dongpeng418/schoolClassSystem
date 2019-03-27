package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.Pair;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.service.RegionService;
import cn.com.school.classinfo.service.TransferApiService;
import cn.com.school.classinfo.utils.RegionUtil;
import cn.com.school.classinfo.vo.RegionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 区域接口
 * @author dongpp
 * @date 2018/10/23
 */
@Api(tags = "区域相关接口", position = 2)
@RestController
@RequestMapping("/api/region")
public class RegionController {

    private final TransferApiService transferApiService;

    private final RegionService regionService;

    @Autowired
    public RegionController(TransferApiService transferApiService,
                            RegionService regionService) {
        this.transferApiService = transferApiService;
        this.regionService = regionService;
    }

    @PostConstruct
    public void init(){
        RegionUtil.regionList(regionService.getAllRegion(2));
    }

    /**
     * 同步青岛城市接口数据
     * @return 是否同步成功
     */
    @ApiIgnore
    @GetMapping("/sync")
    public ResultMessage refresh(){
        ResultMessage message;
        Pair<Boolean, String> citys = transferApiService.getCityList();
        if(citys.getLeft()){
            regionService.saveRegions(citys.getRight());
            message = ResultMessage.success();
        }else{
            message = ResultMessage.apiError(citys.getRight());
        }
        return message;
    }

    /**
     * 区域列表-树形结构
     * @return 区域列表
     */
    @ApiOperation(value = "区域列表-树形结构")
    @GetMapping("/tree")
    public ResultMessage tree(@ApiParam(value = "查询区域级别：1、省以上，2：市以上，3：区县以上")
                              @RequestParam(defaultValue = "2") Integer level) {
        List<RegionVO> regionVOList = regionService.getAllRegionByTree(level);
        return ResultMessage.success(regionVOList);
    }

    /**
     * 区域列表-拼音结构
     * @return 区域列表
     */
    @ApiOperation(value = "区域列表-拼音结构")
    @GetMapping("/py")
    public ResultMessage pinyin(){
        List<RegionVO> regionVOList = regionService.getCityRegionByPy();
        return ResultMessage.success(regionVOList);
    }

    /**
     * 区域列表-拼音结构
     * @return 区域列表
     */
    @ApiOperation(value = "区域列表-拼音结构（列表）")
    @GetMapping("/pyl")
    public ResultMessage pinyin2(){
        List<RegionVO> regionVOList = regionService.getCityRegionByPyList();
        return ResultMessage.success(regionVOList);
    }

    /**
     * 过滤城市列表
     * @param name 查询的名称或拼音
     * @return 城市列表
     */
    @ApiOperation(value = "过滤城市列表")
    @GetMapping("/filter")
    public ResultMessage filter(@RequestParam String name){
        if(StringUtils.isBlank(name)){
            return ResultMessage.paramError("name");
        }
        List<RegionVO> regionVOList = regionService.getCityRegionListByQuery(name);
        return ResultMessage.success(regionVOList);
    }



}
