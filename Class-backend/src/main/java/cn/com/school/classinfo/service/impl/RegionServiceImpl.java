package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.mapper.RegionMapper;
import cn.com.school.classinfo.model.Region;
import cn.com.school.classinfo.service.RegionService;
import cn.com.school.classinfo.vo.RegionVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 区域服务实现类
 *
 * @author dongpp
 * @date 2018/10/24
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "REGION")
@Transactional(rollbackFor = Exception.class)
public class RegionServiceImpl implements RegionService {

    private final RegionMapper regionMapper;

    private static final String[] LETTER_ARRAY = {
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"
    };

    @Autowired
    public RegionServiceImpl(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    /**
     * 同步青岛城市列表接口数据到Region表
     *
     * @param json 区域JSON
     */
    @Override
    public void saveRegions(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(json);
        } catch (IOException e) {
            log.error("parse region json error", e);
        }
        if (Objects.isNull(node)) {
            return;
        }
        JsonNode items = node.get("items");
        List<Region> regionList = Lists.newArrayList();
        for (int i = 0; i < items.size(); i++) {
            Region region = getRegion(items.get(i), "0", 1, "provinceCode", "provinceName");
            regionList.add(region);
            JsonNode citys = items.get(i).get("citys");
            for (int j = 0; j < citys.size(); j++) {
                Region city = getRegion(citys.get(j), region.getRegionKey(), 2, "cityCode", "cityName");
                regionList.add(city);
                JsonNode dists = citys.get(j).get("dists");
                for (int k = 0; k < dists.size(); k++) {
                    Region dist = getRegion(dists.get(k), city.getRegionKey(), 3, "distCode", "distName");
                    regionList.add(dist);
                }
            }
        }
        regionMapper.batchInsert(regionList);
    }

    /**
     * 获取所有区域的树型结构信息
     *
     * @return List
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #level")
    public List<RegionVO> getAllRegionByTree(Integer level) {
        List<Region> regionList = regionMapper.getAll(level);
        List<RegionVO> voList = Lists.newArrayList();
        Map<String, RegionVO> voMap = Maps.newHashMap();
        regionList.forEach(region -> {
            RegionVO regionVO = new RegionVO();
            BeanUtils.copyProperties(region, regionVO);
            voMap.put(region.getRegionKey(), regionVO);
        });
        regionList.forEach(region -> {
            RegionVO vo = voMap.get(region.getRegionKey());
            RegionVO parent = voMap.get(vo.getParentKey());
            if (Objects.isNull(parent)) {
                voList.add(vo);
            } else {
                parent.getItems().add(vo);
            }
        });
        return voList;
    }

    /**
     * 获取所有区域的树型结构信息
     *
     * @param level 区域级别
     * @return List
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #level")
    public List<RegionVO> getAllRegion(Integer level) {
        List<Region> regionList = regionMapper.getAll(level);
        return regionList.stream().map(region -> {
            RegionVO vo = new RegionVO();
            BeanUtils.copyProperties(region, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取城市列表，使用拼音进行归类
     *
     * @return key: 城市拼音首字母，value: 相同字母的城市
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName")
    public List<RegionVO> getCityRegionByPy() {
        List<Region> regionList = regionMapper.getCityList();
        List<RegionVO> voList = Lists.newArrayList();
        Map<String, RegionVO> voMap = Maps.newHashMap();
        for (String letter : LETTER_ARRAY) {
            RegionVO regionVO = new RegionVO();
            regionVO.setPyKey(letter);
            voMap.put(letter, regionVO);
            voList.add(regionVO);
        }
        regionList.forEach(region -> {
            RegionVO regionVO = new RegionVO();
            BeanUtils.copyProperties(region, regionVO);
            String py = region.getRegionCode().substring(0, 1).toUpperCase();
            RegionVO parent = voMap.get(py);
            parent.getItems().add(regionVO);
        });
        return voList;
    }

    /**
     * 获取城市列表，将拼音首字母放入pyKey中并排序
     *
     * @return 城市列表
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName")
    public List<RegionVO> getCityRegionByPyList() {
        List<Region> regionList = regionMapper.getCityList();
        return regionList.stream().map(region -> {
            RegionVO regionVO = new RegionVO();
            BeanUtils.copyProperties(region, regionVO);
            String py = region.getRegionCode().substring(0, 1).toUpperCase();
            regionVO.setPyKey(py);
            return regionVO;
        }).sorted(Comparator.comparing(RegionVO::getRegionCode)).collect(Collectors.toList());
    }

    /**
     * 根据条件获取城市列表
     *
     * @param name 名称或拼音
     * @return 城市列表
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':'  + #root.methodName + ':' + #name")
    public List<RegionVO> getCityRegionListByQuery(String name) {
        List<Region> regionList = regionMapper.getCityListByQuery(name);
        return regionList.stream().map(region -> {
            RegionVO regionVO = new RegionVO();
            BeanUtils.copyProperties(region, regionVO);
            return regionVO;
        }).sorted(Comparator.comparing(RegionVO::getRegionCode)).collect(Collectors.toList());
    }

    /**
     * 创建Region实例
     *
     * @param node      JsonNode
     * @param parentKey 父编码
     * @param level     级别
     * @param code      JSON属性编码
     * @param name      JSON属性名称
     * @return Region实例
     */
    private Region getRegion(JsonNode node, String parentKey, Integer level, String code, String name) {
        Region region = new Region();
        String regionCode, regionName, lon, lat;
        regionCode = node.get(code).asText();
        regionName = node.get(name).asText();
        region.setRegionKey(UUID.randomUUID().toString().replace("-", ""));
        region.setRegionCode(regionCode);
        region.setRegionName(regionName);
        region.setParentKey(parentKey);
        region.setLevel(level);
        region.setCreateBy("sys");
        region.setUpdateBy("sys");
        if (Objects.nonNull(node.get("lon"))) {
            lon = node.get("lon").asText();
            lat = node.get("lat").asText();
            region.setLon(Double.valueOf(lon));
            region.setLat(Double.valueOf(lat));
        }
        return region;
    }

}
