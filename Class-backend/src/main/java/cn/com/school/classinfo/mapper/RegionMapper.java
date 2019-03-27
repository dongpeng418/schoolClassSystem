package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.Region;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 区域表Mapper
 *
 * @author dongpp
 * @date 2018/10/23
 */
public interface RegionMapper {

    /**
     * 批量插入区域信息
     *
     * @param regionList 区域信息列表
     * @return 数量
     */
    @Insert("<script>" +
            "insert into region(" +
            "   region_key, region_code, region_name, parent_key," +
            "   level, lon, lat, create_time, create_by, update_time, update_by" +
            ") " +
            "values" +
            "<foreach collection ='list' item='region' separator =','> " +
            "(" +
            "   #{region.regionKey}, #{region.regionCode}, #{region.regionName}, #{region.parentKey}, #{region.level}," +
            "   #{region.lon}, #{region.lat}, now(), #{region.createBy}, now(), #{region.updateBy}" +
            ")" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<Region> regionList);

    /**
     * 查询所有区域信息
     *
     * @return 区域列表
     */
    @Select("select" +
            "   region_key, region_code,region_name, parent_key, level, lon, lat " +
            " from region" +
            " where level <= #{level} order by level, first_py, region_code")
    @Results(id = "regionColumn", value = {
            @Result(property = "regionKey", column = "region_key"),
            @Result(property = "regionCode", column = "region_code"),
            @Result(property = "regionName", column = "region_name"),
            @Result(property = "parentKey", column = "parent_key"),
            @Result(property = "level", column = "level"),
            @Result(property = "lon", column = "lon"),
            @Result(property = "lat", column = "lat"),
    })
    List<Region> getAll(@Param("level") Integer level);

    /**
     * 查询城市信息
     *
     * @return 区域列表
     */
    @Select("select" +
            "   region_key, region_code,region_name, parent_key, level, lon, lat " +
            " from region" +
            " where level = 2" +
            " order by first_py")
    @ResultMap("regionColumn")
    List<Region> getCityList();

    /**
     * 查询城市信息
     * @param name 名称或拼音
     * @return 区域列表
     */
    @Select("select" +
            "   region_key, region_code,region_name, parent_key, level, lon, lat " +
            " from region" +
            " where level = 2 " +
            "   and (region_name LIKE CONCAT('%',#{name},'%') " +
            "   or region_py LIKE CONCAT('%',#{name},'%'))" +
            "  order by first_py"
    )
    @ResultMap("regionColumn")
    List<Region> getCityListByQuery(String name);

}
