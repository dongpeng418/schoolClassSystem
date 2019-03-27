package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.EvaluationBeta;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 雷达图信息Mapper
 * @author dongpp
 * @date 2018/11/9
 */
public interface EvaluationBetaMapper {

    /**
     * 批量插入雷达图信息
     * @param betas 雷达图信息列表
     * @return 数量
     */
    @Insert("<script>" +
            "insert into evaluation_beta(" +
            "  eval_code," +
            "  name," +
            "  title," +
            "  value," +
            "  create_time," +
            "  create_by," +
            "  update_time," +
            "  update_by" +
            ")" +
            " values " +
            "<foreach collection ='list' item='beta' separator =','> "+
            " (" +
            "  #{beta.evalCode}," +
            "  #{beta.name}," +
            "  #{beta.title}," +
            "  #{beta.value}," +
            "  now()," +
            "  #{beta.createBy}," +
            "  now()," +
            "  #{beta.updateBy}" +
            ")" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<EvaluationBeta> betas);

    /**
     * 根据估价唯一编码查询雷达图信息
     * @param evalCode 估价唯一编码
     * @return 雷达图信息列表
     */
    @Select("select " +
            "  eval_code, name, title, value " +
            "from evaluation_beta" +
            " where eval_code = #{evalCode}")
    @Results({
            @Result(column = "eval_code", property = "evalCode"),
            @Result(column = "name", property = "name"),
            @Result(column = "title", property = "title"),
            @Result(column = "value", property = "value")
    })
    List<EvaluationBeta> select(@Param("evalCode") String evalCode);
}
