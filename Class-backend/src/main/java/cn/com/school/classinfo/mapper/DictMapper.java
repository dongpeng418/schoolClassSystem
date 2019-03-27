package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.Dict;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 字典Mapper
 * @author dongpp
 * @date 2018/10/25
 */
public interface DictMapper {

    /**
     * 获取字典列表
     * @param dictTypeCode 字典类型，可以为空，为空则查询全部字典
     * @return 字典列表
     */
    @Select("<script>" +
            "select " +
            "   dict_code, dict_name, dict_type_code, dict_type_name" +
            " from dict " +
            "<if test='dictTypeCode != null'>" +
            " where dict_type_code = #{dictTypeCode}" +
            "</if>"+
            "</script>")
    @Results({
            @Result(column = "dict_code", property = "dictCode"),
            @Result(column = "dict_name", property = "dictName"),
            @Result(column = "dict_type_code", property = "dictTypeCode"),
            @Result(column = "dict_type_name", property = "dictTypeName"),
    })
    List<Dict> select(@Param("dictTypeCode") String dictTypeCode);
}
