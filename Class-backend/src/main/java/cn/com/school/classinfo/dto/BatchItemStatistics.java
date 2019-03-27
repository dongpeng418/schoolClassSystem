package cn.com.school.classinfo.dto;

import lombok.Data;

/**
 * 批量估价条目统计信息
 * @author dongpp
 * @date 2018/10/30
 */
@Data
public class BatchItemStatistics {

    /**
     * 状态
     */
    private Integer evalState;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 总额
     */
    private Double totalAmount;
}
