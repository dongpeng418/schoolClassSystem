package cn.com.school.classinfo.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.com.school.classinfo.utils.RedisUtil.key;

/**
 * 批量估价队列
 * @author dongpp
 * @date 2018/10/27
 */
@Component
public class EvaluationBatchQueue {

    /**
     * Redis List键名
     */
    private static final String MODULE_KEY = "BATCH";

    private BoundListOperations<String, String> boundListOperations;

    @Autowired
    public EvaluationBatchQueue(RedisTemplate<String, String> template) {
        this.boundListOperations = template.boundListOps(key(MODULE_KEY, "TASK-CODE"));
    }

    /**
     * 从队列右侧获取taskCode
     * @return taskCode
     */
    public String take(){
        return boundListOperations.leftPop();
    }

    /**
     * 返回start到end（包含）之间的taskCode，
     * @param start 起始索引
     * @param end 结束索引
     * @return 范围内的taskCode列表
     */
    public List<String> range(int start, int end){
        return boundListOperations.range(start, end);
    }

    /**
     * 从队列左侧保存taskCode
     * @param taskCode 任务编码
     */
    public void offer(String taskCode){
        boundListOperations.rightPush(taskCode);
    }

    /**
     * 从队列左侧保存taskCodes
     * @param taskCodes 任务编码列表
     */
    public void offerList(List<String> taskCodes){
        boundListOperations.rightPushAll(taskCodes.toArray(new String[]{}));
    }

    /**
     * 获取队列长度
     * @return 队列长度
     */
    public Long size(){
        return boundListOperations.size();
    }

}
