package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.Pair;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.dto.ApiUser;
import cn.com.school.classinfo.dto.HouseInfo;
import cn.com.school.classinfo.service.TransferApiService;
import cn.com.school.classinfo.utils.HttpClientUtil;
import cn.com.school.classinfo.utils.JsonUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author dongpp
 * @date 2018/10/22
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "TRANS_API")
public class TransferApiServiceImpl implements TransferApiService {

    private final AvsConfig avsConfig;

    private static final int SO_TIMEOUT = 20 * 1000;
    private static final int ASSESS_SO_TIMEOUT = 20 * 1000;

    private static final String ADDRESSMORE_URI = "/csfc/v2/ha/parse/addressmore";
    private static final String HA_DETAIL_URI = "/csfc/v2/ha/detail";
    private static final String HA_LIST_URI = "/csfc/v2/ha/list";
    private static final String ANALYSIS_URI = "/statis/v2/price/analysis";
    private static final String DISTRIBUTION_URI = "/statis/v2/price/distribution";
    private static final String SURVEY_URI = "/statis/v2/price/survey";
    private static final String BR_URI = "/statis/v2/price/br";
    private static final String ASSESS_URI = "/avm/v2/assess/directAssess";
    private static final String AROUND_URI = "/csfc/v2/ha/around";
    private static final String CITY_LIST_URI = "/csfc/v2/base/cityList";
    private static final String SMS_URI = "/sup/v2/phone-notify";
    private static final String LOGIN_URI = "/sup/v2/user/login";
    private static final String BATCH_UPLOAD_URI = "/avm/v2/batch/importExcelSuit";
    private static final String BATCH_LIST_URI = "/avm/v2/batch/batchAvmHisList";
    private static final String BATCH_FILE_URI = "/v1/rest/comdata/exportExcelSuit";

    @Autowired
    public TransferApiServiceImpl(AvsConfig avsConfig) {
        this.avsConfig = avsConfig;
    }

    /**
     * 地址清洗，只返回小区列表
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:cityhouse_v2#%E4%BD%8F%E5%9D%80%E6%B8%85%E6%B4%97_%E8%BF%94%E5%9B%9E%E5%A4%9A%E4%B8%AA%E5%B0%8F%E5%8C%BA
     *
     * @param query
     * @param city
     * @param dist
     * @return
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #query + ':' + #city + ':' + #limit", unless = "#result.left == false")
    public Pair<Boolean, String> addressMore(String query, String city, String dist, Integer limit) {
        String url = this.avsConfig.getApi().getCityHouseHttpPrefix() + ADDRESSMORE_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(4);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("s", StringUtils.trim(query));
        params.put("city", StringUtils.trim(city));
        params.put("limit", String.valueOf(limit));
        if (StringUtils.isNotBlank(dist)) {
            params.put("dist", StringUtils.trim(dist));
        }
        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

    /**
     * 获取小区详情
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:cityhouse_v2#%E5%B0%8F%E5%8C%BA%E8%AF%A6%E7%BB%86
     *
     * @param id
     * @param city
     * @return
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #id + ':' + #city", unless = "#result.left == false")
    public Pair<Boolean, String> getHaDetail(String id, String city) {
        String url = this.avsConfig.getApi().getCityHouseHttpPrefix() + HA_DETAIL_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(3);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("id", StringUtils.trim(id));
        params.put("city", StringUtils.trim(city));

        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

    /**
     * 获取中心点周边小区列表
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:cityhouse_v2#%E5%B0%8F%E5%8C%BA%E5%88%97%E8%A1%A8
     *
     * @param city
     * @param proptype
     *            住宅-11，办公-21，商铺-22
     * @param lat
     * @param lon
     * @param distance
     * @param limit
     * @return
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #city + ':' + #proptype + #lon + #lat + #distance + #limit", unless = "#result.left == false")
    public Pair<Boolean, String> getHaListByPoint(String city, String proptype, double lon,
                                                  double lat, int distance, int limit) {
        String url = this.avsConfig.getApi().getCityHouseHttpPrefix() + HA_LIST_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(6);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("city", StringUtils.trim(city));
        params.put("proptype", proptype);
        params.put("location", lon + "|" + lat + "|" + distance);
        params.put("percount", Integer.toString(limit));
        params.put("ob", "d1");

        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }


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
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #city + ':' + #proptype + ':' + #haCode + ':' + #distance", unless = "#result.left == false")
    public Pair<Boolean, String> getHaListByHaCode(String city, String proptype, String haCode, int distance){
        String url = this.avsConfig.getApi().getCityHouseHttpPrefix() + HA_LIST_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(6);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("city", StringUtils.trim(city));
        params.put("proptype", proptype);
        params.put("location", haCode + "|" + distance);
        params.put("ob", "d1");
        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

    /**
     * 查询城市和楼盘小区行情概况
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:creprice_v2#%E5%9F%8E%E5%B8%82_%E6%88%BF%E4%BB%B7_%E7%A7%9F%E9%87%91_%E5%92%8C%E6%A5%BC%E7%9B%98%E5%B0%8F%E5%8C%BA_%E6%88%BF%E4%BB%B7_%E7%A7%9F%E9%87%91_%E6%A6%82%E5%86%B5
     *
     * @param city
     * @param haCode
     * @param propType
     *            住宅-11，办公-21，商铺-22
     * @param tradeType
     *            可以多选，用英文逗号分隔：forsale=售[live=近一个月 history=上个月 day=今日
     *            forcast=预测]；lease=租[live=近一个月 history=上个月 day=今日
     *            forcast=预测]；newha=新盘[history=上个月]
     * @return
     */
    @Override
    public Pair<Boolean, String> getSurvey(String city, String haCode, String propType, String tradeType) {
        String url = this.avsConfig.getApi().getCreValueHttpPrefix() + SURVEY_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(5);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("city", StringUtils.trim(city));
        params.put("ha", StringUtils.trim(haCode));
        params.put("propType", propType);
        params.put("tradeType", StringUtils.trim(tradeType));

        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

    /**
     * 行情走势
     * http://10.10.10.8/wiki/doku.php?id=cityreedi:creprice_v2#%E8%A1%8C%E6%83%85%E8%B5%B0%E5%8A%BF
     *
     * @param city
     * @param haCode
     * @param propType
     *            住宅-11，办公-21，商铺-22
     * @param tradeType
     *            forsale=售 lease=租 newha=新盘价(忽略statKey)
     * @param sinceYear
     *            1、2、5、10、20、9999,若是其它值则认为是 1
     * @param statType
     *            price=单价 total=总价 area=面积 count=样本量 slratio=售租比
     * @return
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #city + ':' + #haCode + ':' + #propType + ':' + #tradeType + ':' + #sinceYear + ':' + #statType", unless = "#result.left == false")
    public Pair<Boolean, String> getAnalysis(String city, String haCode, String propType, String tradeType,
                                             String sinceYear, String statType) {
        String url = this.avsConfig.getApi().getCreValueHttpPrefix() + ANALYSIS_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(8);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("city", StringUtils.trim(city));
        params.put("ha", StringUtils.trim(haCode));
        params.put("propType", propType);
        params.put("tradeType", tradeType);
        params.put("sinceYear", sinceYear);
        params.put("statType", statType);
        params.put("statKey", "supply");

        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

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
     * @return 行情分布信息
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #city + ':' + #haCode + ':' + #propType + ':' + #tradeType + ':' + #statType", unless = "#result.left == false")
    public Pair<Boolean, String> getDistribution(String city, String haCode, String propType, String tradeType,
                                                 String statType) {
        String url = this.avsConfig.getApi().getCreValueHttpPrefix() + DISTRIBUTION_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(8);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("city", StringUtils.trim(city));
        params.put("ha", StringUtils.trim(haCode));
        params.put("propType", propType);
        params.put("tradeType", tradeType);
        params.put("statType", statType);
        params.put("statKey", "supply");

        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }


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
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #city + ':' + #haCode + ':' + #propType + ':' + #tradeType + ':' + #statKey + ':' + #sinceYear", unless = "#result.left == false")
    public Pair<Boolean, String> getBr(String city, String haCode, String propType, String tradeType,
                                       String sinceYear, String statKey){
        String url = this.avsConfig.getApi().getCreValueHttpPrefix() + BR_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(8);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("city", StringUtils.trim(city));
        params.put("ha", StringUtils.trim(haCode));
        params.put("propType", propType);
        params.put("tradeType", tradeType);
        params.put("sinceYear", sinceYear);
        params.put("statKey", statKey);

        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

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
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #houseInfo.hashCode() + ':' + #flag", unless = "#result.left == false")
    public Pair<Boolean, String> rapid(HouseInfo houseInfo, String flag){
        return assess(houseInfo, flag, "1");
    }

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
    @Override
    public Pair<Boolean, String> formal(HouseInfo houseInfo, String flag){
        return assess(houseInfo, flag, "2");
    }

    /**
     * 直接评估
     * @param houseInfo 房屋信息
     * @param flag 估价方法
     * @return 估价结果
     */
    private Pair<Boolean, String> assess(HouseInfo houseInfo, String flag, String rtype) {
        String url = this.avsConfig.getApi().getCreValueHttpPrefix() + ASSESS_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(5);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("flag", flag);
        params.put("rtype", rtype);
        params.put("isSaveSuit", "N");

        String houseJson = JsonUtil.toJson(houseInfo);
        return HttpClientUtil.requestWithBodyByHttpPost(url, params, houseJson, ASSESS_SO_TIMEOUT);
    }

    /**
     * 小区周边环境
     * @param city 城市码
     * @param ha 小区名称或小区码
     * @return 周边环境
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #city + ':' + #ha", unless = "#result.left == false")
    public Pair<Boolean, String> getAround(String city, String ha) {
        String url = this.avsConfig.getApi().getCreValueHttpPrefix() + AROUND_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(8);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("city", StringUtils.trim(city));
        params.put("haName", StringUtils.trim(ha));

        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

    /**
     * 全国行政区域数据
     * @return 区域数据
     */
    @Override
    public Pair<Boolean, String> getCityList(){
        String url = this.avsConfig.getApi().getCityHouseHttpPrefix() + CITY_LIST_URI;
        Map<String, String> params = Maps.newHashMapWithExpectedSize(5);
        params.put("apiKey", avsConfig.getApi().getApiKey());
        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

    /**
     * 发送短信消息
     * @param phoneNumber 手机号
     * @param template 阿里大鱼模板
     *              SMS_77375039 : 通知用户数据下载已准备好
     * @param extras 模板可附加信息，则传入JSON格式内容,模板未指定，则不需要传入
     * @return 送达状态
     */
    @Override
    public Pair<Boolean, String> sendSms(String phoneNumber, String template, Map<String, String> extras){
        String url = this.avsConfig.getApi().getCreValueHttpsPrefix() + SMS_URI;
        Map<String, String> params = Maps.newHashMap();
        params.put("apiKey", avsConfig.getApi().getApiKey());
        params.put("type", "sms");
        params.put("template", template);
        params.put("phone", phoneNumber);
        params.put("extra", JsonUtil.toJson(extras));
        return HttpClientUtil.requestByHttpGet(url, params, SO_TIMEOUT);
    }

    /**
     * 上传批量估价文件并评估
     * @param file 批量估价文件
     * @param user 接口认证用户信息
     * @return 上传结果
     * @throws IOException 处理文件异常
     */
    @Override
    public Pair<Boolean, String> uploadBatchFile(File file, ApiUser user) throws IOException {
        String url = this.avsConfig.getApi().getCreValueHttpPrefix() + BATCH_UPLOAD_URI;
        Map<String, ContentBody> params = Maps.newHashMapWithExpectedSize(5);
        params.put("apiKey", new StringBody(avsConfig.getApi().getApiKey(), ContentType.MULTIPART_FORM_DATA));
        params.put("userToken", new StringBody(user.getUserToken(), ContentType.MULTIPART_FORM_DATA));
        params.put("type", new StringBody("1", ContentType.MULTIPART_FORM_DATA));
        params.put("file", new InputStreamBody(FileUtils.openInputStream(file), ContentType.MULTIPART_FORM_DATA, file.getName()));
        return HttpClientUtil.requestWithMultipartByHttpPost(url, params, SO_TIMEOUT);
    }

}
