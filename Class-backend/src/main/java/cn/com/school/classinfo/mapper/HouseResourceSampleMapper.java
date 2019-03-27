package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.HouseResourceSample;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 房源样例Mapper
 *
 * @author dongpp
 * @date 2018/10/24
 */
public interface HouseResourceSampleMapper {

    /**
     * 批量插入房源样例信息
     *
     * @param samples 房源样例列表
     * @return 数量
     */
    @Insert("<script>" +
            "insert into house_resource_sample(" +
            "  eval_code, hr_id, ha_code, ha_name, dist_code, dist_name, street_code," +
            "  street_name, street_no, prop_type_code, prop_type_name, unit_price, unit_price_unit, " +
            "  total_price, total_price_unit, area_size, floor, bheight, face_code, face_name," +
            "  intdeco_code, intdeco_name, br, lr, cr, ba, offer_time," +
            "  create_time, create_by, update_time, update_by) " +
            "values" +
            "<foreach collection ='list' item='house' separator =','> "+
            " (" +
            "  #{house.evalCode}, #{house.hrId}, #{house.haCode}, #{house.haName}, #{house.distCode}," +
            "  #{house.distName}, #{house.streetCode}, #{house.streetName}, #{house.streetNo}, #{house.propTypeCode}, #{house.propTypeName}," +
            "  #{house.unitPrice}, #{house.unitPriceUnit}, #{house.totalPrice}, #{house.totalPriceUnit}, #{house.areaSize}, " +
            "  #{house.floor}, #{house.bheight}, #{house.faceCode}, #{house.faceName}, #{house.intdecoCode}, #{house.intdecoName}," +
            "  #{house.br}, #{house.lr}, #{house.cr}, #{house.ba}, #{house.offerTime}, now(), #{house.createBy}," +
            "  now(), #{house.updateBy}" +
            " )" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<HouseResourceSample> samples);

    /**
     * 根据估价唯一码查询房源信息
     * @param evalCode 估价唯一码
     * @return 房源信息列表
     */
    @Select("select " +
            "  id, eval_code, hr_id, ha_code, ha_name, dist_code, dist_name, street_code, " +
            "  street_name, street_no, prop_type_code, prop_type_name, unit_price, unit_price_unit, " +
            "  total_price, total_price_unit, area_size, floor, bheight, face_code, face_name, " +
            "  intdeco_code, intdeco_name, br, lr, cr, ba, offer_time" +
            " from house_resource_sample where eval_code = #{code}")
    @Results(id = "hrsColumn", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "eval_code", property = "evalCode"),
            @Result(column = "hr_id", property = "hrId"),
            @Result(column = "ha_code", property = "haCode"),
            @Result(column = "ha_name", property = "haName"),
            @Result(column = "dist_code", property = "distCode"),
            @Result(column = "dist_name", property = "distName"),
            @Result(column = "street_code", property = "streetCode"),
            @Result(column = "street_name", property = "streetName"),
            @Result(column = "street_no", property = "streetNo"),
            @Result(column = "prop_type_code", property = "propTypeCode"),
            @Result(column = "prop_type_name", property = "propTypeName"),
            @Result(column = "unit_price", property = "unitPrice"),
            @Result(column = "unit_price_unit", property = "unitPriceUnit"),
            @Result(column = "total_price", property = "totalPrice"),
            @Result(column = "total_price_unit", property = "totalPriceUnit"),
            @Result(column = "area_size", property = "areaSize"),
            @Result(column = "floor", property = "floor"),
            @Result(column = "bheight", property = "bheight"),
            @Result(column = "face_code", property = "faceCode"),
            @Result(column = "face_name", property = "faceName"),
            @Result(column = "intdeco_code", property = "intdecoCode"),
            @Result(column = "intdeco_name", property = "intdecoName"),
            @Result(column = "br", property = "br"),
            @Result(column = "lr", property = "lr"),
            @Result(column = "cr", property = "cr"),
            @Result(column = "ba", property = "ba"),
            @Result(column = "offer_time", property = "offerTime"),
    })
    List<HouseResourceSample> selectByEvalCode(String evalCode);
}
