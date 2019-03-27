package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.ObjectionQuery;
import cn.com.school.classinfo.model.EvaluationObjection;

import java.util.List;

/**
 * 估价异议Mapper
 *
 * @author dongpp
 * @date 2018/10/28
 */
public interface EvaluationObjectionMapper {

    /**
     * 插入估价异议
     *
     * @param objection 估价异议
     * @return 数量
     */
    int insert(EvaluationObjection objection);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<EvaluationObjection> selectListByQuery(ObjectionQuery query);

    /**
     * 根据ID查询估价异议记录
     *
     * @param objectionQuery 查询条件
     * @return 估价异议
     */
    EvaluationObjection selectByQuery(ObjectionQuery objectionQuery);

}
