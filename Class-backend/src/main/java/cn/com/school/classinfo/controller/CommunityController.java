package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.Pair;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.service.TransferApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/**
 * 小区相关接口
 *
 * @author dongpp
 * @date 2018/10/24
 */
@Api(tags = "小区相关接口", position = 3)
@Validated
@RestController
@RequestMapping("/api/community")
public class CommunityController {

    private final TransferApiService transferApiService;

    @Autowired
    public CommunityController(TransferApiService transferApiService) {
        this.transferApiService = transferApiService;
    }

    /**
     * 小区清洗接口
     *
     * @param name 小区名称
     * @param city 城市名或码
     * @param dist 行政区名或码
     * @return 小区列表
     */
    @ApiOperation("小区清洗接口")
    @GetMapping("/filter")
    public ResultMessage filter(@ApiParam(value = "小区", required = true)
                                @NotBlank(message = "name 不能为空")
                                @RequestParam String name,
                                @ApiParam(value = "城市", required = true)
                                @NotBlank(message = "city 不能为空")
                                @RequestParam String city,
                                @ApiParam(value = "区县") @RequestParam(required = false) String dist,
                                @ApiParam(value = "最多返回行数，0 不限制，默认10条")
                                    @RequestParam(required = false, defaultValue = "10") Integer limit) {
        Pair<Boolean, String> result = transferApiService.addressMore(StringUtils.trimToEmpty(name), city, dist, limit);
        return ResultMessage.transfer(result);
    }

    /**
     * 获取小区详情
     *
     * @param ha   小区编码或准确名称
     * @param city 城市名称或编码
     * @return 小区详情
     */
    @ApiOperation("小区详情接口")
    @GetMapping(path = "/detail")
    public ResultMessage detail(@ApiParam(value = "小区", required = true)
                                @NotBlank(message = "ha 不能为空")
                                @RequestParam String ha,
                                @ApiParam(value = "城市", required = true)
                                @NotBlank(message = "city 不能为空")
                                @RequestParam String city) {
        Pair<Boolean, String> result = transferApiService.getHaDetail(ha, StringUtils.trimToEmpty(city));
        return ResultMessage.transfer(result);
    }

    /**
     * 获取坐标周边小区列表
     *
     * @param city     城市编码
     * @param propType 用途 住宅-11，办公-21，商铺-22
     * @param lon      经度
     * @param lat      纬度
     * @param distance 范围
     * @param limit    限制
     * @return 小区列表
     */
    @ApiOperation("获取坐标周边小区列表接口")
    @GetMapping("/list")
    public ResultMessage getList(@ApiParam(value = "城市", required = true)
                                     @NotBlank(message = "city 不能为空")
                                     @RequestParam String city,
                                 @ApiParam(value = "用途（住宅-11，办公-21，商铺-22）")
                                     @Pattern(regexp = "11|21|22", message = "propType 参数错误")
                                     @RequestParam(required = false, defaultValue = "11") String propType,
                                 @ApiParam(value = "经度", required = true)
                                     @Positive(message = "lon 参数错误")
                                     @RequestParam double lon,
                                 @ApiParam(value = "纬度", required = true)
                                     @Positive(message = "lat 参数错误")
                                     @RequestParam double lat,
                                 @ApiParam(value = "范围（米）")
                                     @Range(min = 0, max = 2000, message = "distance 区间为0-2000之间")
                                     @RequestParam(required = false, defaultValue = "500") int distance,
                                 @ApiParam(value = "返回数量")
                                     @Range(min = 0, max = 200, message = "limit 区间为0-200之间")
                                     @RequestParam(required = false, defaultValue = "20") int limit) {
        Pair<Boolean, String> result = transferApiService.getHaListByPoint(city, propType, lon, lat, distance, limit);
        return ResultMessage.transfer(result);
    }

    /**
     * 获取小区周边环境接口
     *
     * @param ha   小区编码或准确名称
     * @param city 城市名称或编码
     * @return 小区详情
     */
    @ApiOperation("小区周边环境接口")
    @GetMapping("/around")
    public ResultMessage around(@ApiParam(value = "小区", required = true)
                                @NotBlank(message = "ha 不能为空")
                                @RequestParam String ha,
                                @ApiParam(value = "城市", required = true)
                                @NotBlank(message = "city 不能为空")
                                @RequestParam String city) {
        if (StringUtils.isAnyBlank(ha, city)) {
            return ResultMessage.paramError();
        }
        Pair<Boolean, String> result = transferApiService.getAround(StringUtils.trimToEmpty(city), ha);
        return ResultMessage.transfer(result);
    }

    /**
     * 行情走势
     *
     * @param city      城市码或名称
     * @param ha        小区码
     * @param propType  用途码或者名称
     * @param tradeType forsale=售 lease=租 newha=新盘价(忽略statKey)
     * @param sinceYear 1、2、5、10、20、9999,若是其它值则认为是 1
     * @param statType  price=单价 total=总价 area=面积 count=样本量 slratio=售租比
     * @return 走势信息
     */
    @ApiOperation("小区行情走势接口")
    @GetMapping("/analysis")
    public ResultMessage getAnalysis(@ApiParam(value = "小区", required = true)
                                         @NotBlank(message = "ha 不能为空")
                                         @RequestParam String ha,
                                     @ApiParam(value = "城市", required = true)
                                         @NotBlank(message = "city 不能为空")
                                         @RequestParam String city,
                                     @ApiParam(value = "用途（住宅-11，办公-21，商铺-22）")
                                         @Pattern(regexp = "11|21|22", message = "propType 参数错误")
                                         @RequestParam(required = false, defaultValue = "11") String propType,
                                     @ApiParam(value = "租售类型（forsale=售 lease=租 newha=新盘价）")
                                         @Pattern(regexp = "forsale|lease|newha", message = "tradeType 参数错误")
                                         @RequestParam(required = false, defaultValue = "forsale") String tradeType,
                                     @ApiParam(value = "几年（1、2、5、10、20、9999）")
                                         @Pattern(regexp = "1|2|5|10|20|9999", message = "sinceYear 参数错误")
                                         @RequestParam(required = false, defaultValue = "5") String sinceYear,
                                     @ApiParam(value = "类型（price=单价 total=总价 area=面积 count=样本量 slratio=售租比）")
                                         @Pattern(regexp = "price|total|area|count|slratio", message = "statType 参数错误")
                                         @RequestParam(required = false, defaultValue = "price") String statType) {
        Pair<Boolean, String> result = transferApiService.getAnalysis(city, ha, propType, tradeType, sinceYear, statType);
        return ResultMessage.transfer(result);
    }

    /**
     * 小区户型走势分布接口
     *
     * @param city      城市码或名称
     * @param ha        小区码
     * @param propType  用途码或者名称
     * @param tradeType forsale=售 lease=租
     * @param sinceYear 1、2、5、10、20、9999,若是其它值则认为是 1
     * @param statKey   rate=占比,price=价格，ratePrice=占比价格
     * @return 走势信息
     */
    @ApiOperation("小区户型走势分布接口")
    @GetMapping("/br")
    public ResultMessage getBr(@ApiParam(value = "小区", required = true)
                                   @NotBlank(message = "ha 不能为空")
                                   @RequestParam String ha,
                               @ApiParam(value = "城市", required = true)
                                   @NotBlank(message = "city 不能为空")
                                   @RequestParam String city,
                               @ApiParam(value = "用途（住宅-11，办公-21，商铺-22）")
                                   @Pattern(regexp = "11|21|22", message = "propType 参数错误")
                                   @RequestParam(required = false, defaultValue = "11") String propType,
                               @ApiParam(value = "租售类型（forsale=售 lease=租）")
                                   @Pattern(regexp = "forsale|lease", message = "tradeType 参数错误")
                                   @RequestParam(required = false, defaultValue = "forsale") String tradeType,
                               @ApiParam(value = "几年（1、2、5、10、20、9999）")
                                   @Pattern(regexp = "1|2|5|10|20|9999", message = "sinceYear 参数错误")
                                   @RequestParam(required = false, defaultValue = "5") String sinceYear,
                               @ApiParam(value = "类型（rate=占比,price=价格，ratePrice=占比价格）")
                                   @Pattern(regexp = "rate|price|ratePrice", message = "statKey 参数错误")
                                   @RequestParam(required = false, defaultValue = "ratePrice") String statKey) {

        Pair<Boolean, String> result = transferApiService.getBr(city, ha, propType, tradeType, sinceYear, statKey);
        return ResultMessage.transfer(result);
    }

    /**
     * 小区行情分布接口
     *
     * @param city      城市码或名称
     * @param ha        小区码
     * @param propType  用途码或者名称
     * @param tradeType forsale=售 lease=租 newha=新楼盘(忽略statKey)
     * @param statType  price=单价 total=总价 area=面积
     * @return 行情分布信息
     */
    @ApiOperation("小区行情分布接口")
    @GetMapping("/dist")
    public ResultMessage getDistribution(@ApiParam(value = "小区", required = true)
                                             @NotBlank(message = "ha 不能为空")
                                             @RequestParam String ha,
                                         @ApiParam(value = "城市", required = true)
                                             @NotBlank(message = "city 不能为空")
                                             @RequestParam String city,
                                         @ApiParam(value = "用途（住宅-11，办公-21，商铺-22）")
                                             @Pattern(regexp = "11|21|22", message = "propType 参数错误")
                                             @RequestParam(required = false, defaultValue = "11") String propType,
                                         @ApiParam(value = "租售类型（forsale=售 lease=租 newha=新盘价）")
                                             @Pattern(regexp = "forsale|lease|newha", message = "tradeType 参数错误")
                                             @RequestParam(required = false, defaultValue = "forsale") String tradeType,
                                         @ApiParam(value = "类型（price=单价 total=总价 area=面积）")
                                             @Pattern(regexp = "price|total|area", message = "statType 参数错误")
                                             @RequestParam(required = false, defaultValue = "price") String statType) {
        Pair<Boolean, String> result = transferApiService.getDistribution(city, ha, propType, tradeType, statType);
        return ResultMessage.transfer(result);
    }

    /**
     * 小区房价概况接口
     *
     * @param city      城市码或名称
     * @param ha        小区码
     * @param propType  用途码或者名称
     * @param tradeType 可以多选，用英文逗号分隔
     *                  forsale=售[live=近一个月 history=上个月 day=今日 forcast=预测]
     *                  lease=租[live=近一个月 history=上个月 day=今日 forcast=预测]
     *                  newha=新盘[history=上个月]
     * @return 房价概况
     */
    @ApiIgnore
    @ApiOperation("小区房价概况接口")
    @GetMapping("/price")
    public ResultMessage getSurvey(@ApiParam(value = "小区", required = true)
                                       @NotBlank(message = "ha 不能为空")
                                       @RequestParam String ha,
                                   @ApiParam(value = "城市", required = true)
                                       @NotBlank(message = "city 不能为空")
                                       @RequestParam String city,
                                   @ApiParam(value = "用途（住宅-11，办公-21，商铺-22）")
                                       @Pattern(regexp = "11|21|22", message = "propType 参数错误")
                                       @RequestParam(required = false, defaultValue = "11") String propType,
                                   @ApiParam(value = "租售类型（forsale=售 lease=租 newha=新盘价）")
                                       @RequestParam(required = false, defaultValue = "forsale_history,newha_history") String tradeType) {
        Pair<Boolean, String> result = transferApiService.getSurvey(city, ha, propType, tradeType);
        return ResultMessage.transfer(result);
    }

}
