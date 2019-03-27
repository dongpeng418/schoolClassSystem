package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.constant.BatchConstant;
import cn.com.school.classinfo.common.query.BatchItemQuery;
import cn.com.school.classinfo.common.query.BatchQuery;
import cn.com.school.classinfo.dto.BatchItemStatistics;
import cn.com.school.classinfo.mapper.EvaluationBatchItemMapper;
import cn.com.school.classinfo.mapper.EvaluationBatchMapper;
import cn.com.school.classinfo.model.EvaluationBatch;
import cn.com.school.classinfo.model.EvaluationBatchItem;
import cn.com.school.classinfo.service.EvaluationBatchService;
import cn.com.school.classinfo.utils.EvaluationUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 批量估价服务实现类
 * @author dongpp
 * @date 2018/10/26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EvaluationBatchServiceImpl implements EvaluationBatchService {

    private final EvaluationBatchMapper evaluationBatchMapper;

    private final EvaluationBatchItemMapper evaluationBatchItemMapper;

    @Autowired
    public EvaluationBatchServiceImpl(EvaluationBatchMapper evaluationBatchMapper,
                                      EvaluationBatchItemMapper evaluationBatchItemMapper) {
        this.evaluationBatchMapper = evaluationBatchMapper;
        this.evaluationBatchItemMapper = evaluationBatchItemMapper;
    }

    /**
     * 保存批量估价信息
     * @param batch 批量估价信息
     * @return 批量估价信息
     */
    @Override
    public EvaluationBatch saveEvaluationBatch(EvaluationBatch batch, List<EvaluationBatchItem> itemList){
        batch.setTaskState(BatchConstant.TASK_PROGRESS);
        batch.setTaskCode(EvaluationUtil.generateTaskCode());
        evaluationBatchMapper.insert(batch);
        int size = itemList.size();
        int limit = 1000;
        int skip = 0;
        while (size > 0){
            List<EvaluationBatchItem> partItemList = itemList.stream().skip(skip).limit(limit).collect(Collectors.toList());
            partItemList.forEach(item -> {
                item.setBatchId(batch.getId());
            });
            evaluationBatchItemMapper.batchInsert(partItemList);
            skip += 1000;
            size -= 1000;
        }
        return batch;
    }

    /**
     * 根据条件查询批量估价列表
     * @param query 查询条件
     * @return 估价列表
     */
    @Override
    public PageInfo<EvaluationBatch> getEvaluationBatchList(BatchQuery query){
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        PageHelper.orderBy("submit_time desc");
        List<EvaluationBatch> batchList = evaluationBatchMapper.selectListByQuery(query);

        return new PageInfo<>(batchList);
    }

    /**
     * 查询所有未完成任务的任务号
     * @return 批次号列表
     */
    @Override
    public List<String> getTaskCodeList(){
        return evaluationBatchMapper.selectAllTaskCode();
    }

    /**
     * 查询批量估价记录
     * @param taskCode 任务号
     * @return 批量估价记录
     */
    @Override
    public EvaluationBatch getByTaskCode(String taskCode){
        BatchQuery query = new BatchQuery();
        query.setTaskCode(taskCode);
        return evaluationBatchMapper.selectByQuery(query);
    }

    /**
     * 查询批量估价记录
     * @param query 查询条件
     * @return 批量估价记录
     */
    @Override
    public EvaluationBatch getById(BatchQuery query) {
        return evaluationBatchMapper.selectByQuery(query);
    }

    /**
     * 更新任务状态
     * @param batch 批量估价信息
     */
    @Override
    public void updateState(EvaluationBatch batch){
        evaluationBatchMapper.update(batch);
    }

    /**
     * 保存批量估价条目列表
     * @param batchItems 条目列表
     */
    @Override
    public void saveItemList(List<EvaluationBatchItem> batchItems){
        if(CollectionUtils.isEmpty(batchItems)){
            return;
        }
        evaluationBatchItemMapper.batchInsert(batchItems);
    }

    /**
     * 根据条件查询批量估价条目列表
     * @param query 查询条件
     * @return 条目列表
     */
    @Override
    public PageInfo<EvaluationBatchItem> getBatchItemList(BatchItemQuery query){
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        List<EvaluationBatchItem> batchList = evaluationBatchItemMapper.selectListByQuery(query);
        return new PageInfo<>(batchList);
    }

    /**
     * 根据条件查询批量估价条目列表（不分页，用于导出）
     * @param query 查询条件
     * @return 条目列表
     */
    @Override
    public List<EvaluationBatchItem> getUnPageBatchItemList(BatchItemQuery query){
        return evaluationBatchItemMapper.selectListByQuery(query);
    }

    /**
     * 更新批量估价条目列表
     * @param batchItems 条目列表
     */
    @Override
    public void updateItemList(List<EvaluationBatchItem> batchItems) {
        evaluationBatchItemMapper.batchUpdate(batchItems);
    }

    /**
     * 根据批量估价的ID来获取该批量估价的统计信息
     * @param batchId 批量估价ID
     * @return 总额
     */
    @Override
    public List<BatchItemStatistics> getStatisticsByBatchId(Integer batchId) {
        return evaluationBatchItemMapper.selectStatisticsByBatchId(batchId);
    }
}
