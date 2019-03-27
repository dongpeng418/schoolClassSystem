package cn.com.school.classinfo.service;

import cn.com.school.classinfo.vo.RegionVO;

import java.util.List;

/**
 * 区域Service
 *
 * @author dongpp
 * @date 2018/10/24
 */
public interface RegionService {

    /**
     * 批量保存区域信息
     *
     * @param json 区域JSON
     */
    void saveRegions(String json);

    /**
     * 获取所有区域的树型结构信息
     *
     * @param level 区域级别
     * @return List
     */
    List<RegionVO> getAllRegionByTree(Integer level);

    /**
     * 获取所有区域的树型结构信息
     *
     * @param level 区域级别
     * @return List
     */
    List<RegionVO> getAllRegion(Integer level);

    /**
     * 获取城市列表，使用拼音进行归类
     *
     * @return key: 城市拼音首字母，value: 相同字母的城市
     */
    List<RegionVO> getCityRegionByPy();

    /**
     * 获取城市列表，将拼音首字母放入pyKey中并排序
     *
     * @return 城市列表
     */
    List<RegionVO> getCityRegionByPyList();

    /**
     * 根据条件获取城市列表
     *
     * @param name 名称或拼音
     * @return 城市列表
     */
    List<RegionVO> getCityRegionListByQuery(String name);
}
