package cn.com.school.classinfo.utils;

import cn.com.school.classinfo.vo.RegionVO;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 区域信息表
 *
 * @author dongpp
 * @date 2018/12/4
 */
public class RegionUtil {

    private static List<RegionVO> regionList;

    private static Map<String, RegionVO> regionMap = Maps.newHashMap();

    public static void regionList(List<RegionVO> regions) {
        regionList = regions;
        regionList.forEach(regionVO -> regionMap.put(regionVO.getRegionKey(), regionVO));
    }

    public static RegionVO getParent(String regionCode) {
        RegionVO region = regionList.stream()
                .filter(regionVO -> regionVO.getRegionCode().equalsIgnoreCase(regionCode))
                .findFirst()
                .orElse(null);
        return Objects.isNull(region) ? null : regionMap.get(region.getParentKey());
    }

    /**
     * 获取城市名称
     *
     * @param cityCode 城市编码
     * @return
     */
    public static String getCityName(String cityCode) {
        RegionVO region = regionList.stream()
                .filter(regionVO -> regionVO.getLevel() == 2
                        && regionVO.getRegionCode().equalsIgnoreCase(cityCode))
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(region)) {
            return region.getRegionName();
        }
        return null;
    }

    /**
     * 获取城市编码
     *
     * @param cityName 城市名称
     * @return
     */
    public static String getCityCode(String cityName) {
        RegionVO region = regionList.stream()
                .filter(regionVO -> regionVO.getLevel() == 2
                        && regionVO.getRegionName().equalsIgnoreCase(cityName))
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(region)) {
            return region.getRegionCode();
        }
        return null;
    }
}
