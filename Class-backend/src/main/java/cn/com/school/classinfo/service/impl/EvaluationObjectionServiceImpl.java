package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.query.ObjectionQuery;
import cn.com.school.classinfo.mapper.EvaluationObjectionMapper;
import cn.com.school.classinfo.model.EvaluationObjection;
import cn.com.school.classinfo.service.EvaluationObjectionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 估价异议服务实现类
 *
 * @author dongpp
 * @date 2018/10/28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EvaluationObjectionServiceImpl implements EvaluationObjectionService {

    private final EvaluationObjectionMapper evaluationObjectionMapper;

    @Autowired
    public EvaluationObjectionServiceImpl(EvaluationObjectionMapper evaluationObjectionMapper) {
        this.evaluationObjectionMapper = evaluationObjectionMapper;
    }

    /**
     * 保存估价异议
     *
     * @param objection 估价异议信息
     */
    @Override
    public void save(EvaluationObjection objection) {
        evaluationObjectionMapper.insert(objection);
    }

    /**
     * 估价异议列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public PageInfo<EvaluationObjection> getList(ObjectionQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        PageHelper.orderBy("objection_time desc");
        List<EvaluationObjection> recordList = evaluationObjectionMapper.selectListByQuery(query);
        return new PageInfo<>(recordList);
    }

    /**
     * 根据ID查询估价异议信息
     *
     * @param objectionQuery 查询条件
     * @return 估价异议
     */
    @Override
    public EvaluationObjection getById(ObjectionQuery objectionQuery) {
        return evaluationObjectionMapper.selectByQuery(objectionQuery);
    }

}
