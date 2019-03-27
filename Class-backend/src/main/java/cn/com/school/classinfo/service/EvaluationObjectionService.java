package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.ObjectionQuery;
import cn.com.school.classinfo.model.EvaluationObjection;
import com.github.pagehelper.PageInfo;

/**
 * 估价异议Service
 *
 * @author dongpp
 * @date 2018/10/28
 */
public interface EvaluationObjectionService {

    /**
     * 保存估价异议
     *
     * @param objection 估价异议信息
     */
    void save(EvaluationObjection objection);

    /**
     * 估价异议列表
     *
     * @param query 查询条件
     * @return 列表
     */
    PageInfo<EvaluationObjection> getList(ObjectionQuery query);

    /**
     * 根据ID查询估价异议信息
     *
     * @param objectionQuery 查询条件
     * @return 估价异议
     */
    EvaluationObjection getById(ObjectionQuery objectionQuery);
}
