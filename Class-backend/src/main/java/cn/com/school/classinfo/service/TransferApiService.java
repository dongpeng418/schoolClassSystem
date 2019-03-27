package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.Pair;
import cn.com.school.classinfo.dto.ApiUser;
import cn.com.school.classinfo.dto.HouseInfo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 中转API服务，这里的API都不是本项目实现的接口，而是通过Http Client调用青岛已经实现的接口，然后暴露出接口对外提供服务
 * @author dongpp
 * @date 2018/10/22
 */
public interface TransferApiService {

    /**
     * 地址清洗，只返回小区列表
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:cityhouse_v2#%E4%BD%8F%E5%9D%80%E6%B8%85%E6%B4%97_%E8%BF%94%E5%9B%9E%E5%A4%9A%E4%B8%AA%E5%B0%8F%E5%8C%BA
     * @param query 关键词
     * @param city 城市码
     * @param dist 行政区码
     * @param limit 最多返回行数，0 不限制
     * @return left: 成功/失败，right: 小区列表
     */
    Pair<Boolean, String> addressMore(String query, String city, String dist, Integer limit);

    /**
     * 获取小区详情
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:cityhouse_v2#%E5%B0%8F%E5%8C%BA%E8%AF%A6%E7%BB%86
     *
     * @param id
     * @param city
     * @return
     */
    Pair<Boolean, String> getHaDetail(String id, String city);

    /**
     * 获取中心点周边小区列表
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:cityhouse_v2#%E5%B0%8F%E5%8C%BA%E5%88%97%E8%A1%A8
     *
     * @param city 城市码
     * @param proptype 住宅-11，办公-21，商铺-22
     * @param lon 纬度
     * @param lat 经度
     * @param distance 范围
     * @param limit 限制
     * @return left: 成功/失败，right: 小区列表
     */
    Pair<Boolean, String> getHaListByPoint(String city, String proptype, double lon, double lat,
                                           int distance, int limit);

    /**
     * 根据haCode获取周边小区列表
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:cityhouse_v2#%E5%B0%8F%E5%8C%BA%E5%88%97%E8%A1%A8
     *
     * @param city 城市码
     * @param proptype 住宅-11，办公-21，商铺-22
     * @param haCode 小区码
     * @param distance 范围
     * @return left: 成功/失败，right: 小区列表
     */
    Pair<Boolean, String> getHaListByHaCode(String city, String proptype, String haCode, int distance);

    /**
     * 查询城市和楼盘小区行情概况
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:creprice_v2#%E5%9F%8E%E5%B8%82_%E6%88%BF%E4%BB%B7_%E7%A7%9F%E9%87%91_%E5%92%8C%E6%A5%BC%E7%9B%98%E5%B0%8F%E5%8C%BA_%E6%88%BF%E4%BB%B7_%E7%A7%9F%E9%87%91_%E6%A6%82%E5%86%B5
     *
     * @param city 城市码
     * @param haCode 小区码
     * @param propType 住宅-11，办公-21，商铺-22
     * @param tradeType
     *            可以多选，用英文逗号分隔：forsale=售[live=近一个月 history=上个月 day=今日
     *            forcast=预测]；lease=租[live=近一个月 history=上个月 day=今日
     *            forcast=预测]；newha=新盘[history=上个月]
     * @return left: 成功/失败，right: 小区行情
     */
    Pair<Boolean, String> getSurvey(String city, String haCode, String propType, String tradeType);

    /**
     * 行情走势
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:creprice_v2#%E8%A1%8C%E6%83%85%E8%B5%B0%E5%8A%BF
     *
     * @param city 城市码
     * @param haCode 小区码
     * @param propType
     *            住宅-11，办公-21，商铺-22
     * @param tradeType
     *            forsale=售 lease=租 newha=新盘价(忽略statKey)
     * @param sinceYear
     *            1、2、5、10、20、9999,若是其它值则认为是 1
     * @param statType
     *            price=单价 total=总价 area=面积 count=样本量 slratio=售租比
     * @return 走势数据
     */
    Pair<Boolean, String> getAnalysis(String city, String haCode, String propType, String tradeType,
                                      String sinceYear, String statType);

    /**
     * 行情分布
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:creprice_v2#%E8%A1%8C%E6%83%85%E5%88%86%E5%B8%83
     *
     * @param city 城市码
     * @param haCode 小区码
     * @param propType
     *            住宅-11，办公-21，商铺-22
     * @param tradeType
     *            forsale=售 lease=租 newha=新楼盘(忽略statKey)
     * @param statType
     *            price=单价 total=总价 area=面积
     * @return
     */
    Pair<Boolean, String> getDistribution(String city, String haCode, String propType, String tradeType,
                                          String statType);

    /**
     * 楼盘小区户型走势分布接口
     * @param city 城市码
     * @param haCode 小区码
     * @param propType
     *              住宅-11，办公-21，商铺-22
     * @param tradeType
     *              forsale=售 lease=租
     * @param sinceYear
     *              1、2、5、10、20、9999,若是其它值则认为是 1
     * @param statKey
     *              rate=占比,price=价格，ratePrice=占比价格
     * @return 户型走势信息
     */
    Pair<Boolean, String> getBr(String city, String haCode, String propType, String tradeType,
                                String sinceYear, String statKey);

    /**
     * 小区周边环境
     * @param city 城市码
     * @param ha 小区名称或小区码
     * @return
     */
    Pair<Boolean, String> getAround(String city, String ha);

    /**
     * 快速评估
     * @param houseInfo 房屋信息
     * @param flag
     *       估价方法
     *       1 - 市场比较法(出售估价)
     *       2 - 市场比较法(出租估价)
     *       3 - 多元回归[小区]分析法(出售估价)
     *       4 - 多元回归[小区]分析法(出租估价)
     *       5 - 标准价格[坐标]调整法(出售估价)
     *       6 - 标准价格[坐标]调整法(出租估价)
     *       7 - 联合估计法(出售估价)
     *       8 - 联合估计法(出租估价)
     *       9 - 收益法(出售估价)
     * @return 估价结果
     */
    Pair<Boolean, String> rapid(HouseInfo houseInfo, String flag);

    /**
     * 正式评估
     * @param houseInfo 房屋信息
     * @param flag
     *       估价方法
     *       1 - 市场比较法(出售估价)
     *       2 - 市场比较法(出租估价)
     *       3 - 多元回归[小区]分析法(出售估价)
     *       4 - 多元回归[小区]分析法(出租估价)
     *       5 - 标准价格[坐标]调整法(出售估价)
     *       6 - 标准价格[坐标]调整法(出租估价)
     *       7 - 联合估计法(出售估价)
     *       8 - 联合估计法(出租估价)
     *       9 - 收益法(出售估价)
     * @return 估价结果
     */
    Pair<Boolean, String> formal(HouseInfo houseInfo, String flag);

    /**
     * 全国行政区域数据
     * @return 区域数据
     */
    Pair<Boolean, String> getCityList();

    /**
     * 发送短信消息
     * @param phoneNumber 手机号
     * @param template 阿里大鱼模板
     *              SMS_77375039 : 通知用户数据下载已准备好
     * @param extras 模板可附加信息，则传入JSON格式内容,模板未指定，则不需要传入
     * @return 送达状态
     */
    Pair<Boolean, String> sendSms(String phoneNumber, String template, Map<String, String> extras);

    /**
     * 上传批量估价文件并评估
     * @param file 批量估价文件
     * @param user 接口认证用户信息
     * @return 上传结果
     * @throws IOException 处理文件异常
     */
    Pair<Boolean, String> uploadBatchFile(File file, ApiUser user) throws IOException;

}
