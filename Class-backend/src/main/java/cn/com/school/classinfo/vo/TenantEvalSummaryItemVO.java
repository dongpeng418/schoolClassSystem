package cn.com.school.classinfo.vo;

import lombok.Data;

/**
 * @author dongpp
 * @date 2018/11/23
 */
@Data
public class TenantEvalSummaryItemVO {

    /**
     * 客户公司ID
     */
    private Integer companyId;

    /**
     * 客户公司名称
     */
    private String companyName;

    /**
     * 客户ID
     */
    private Integer customerId;

    /**
     * 客户帐号
     */
    private String customer;

    /**
     * 公司类别
     */
    private String categoryName;

    /**
     * 小区数
     */
    private Integer ha;

    /**
     * 快速估价数
     */
    private Integer rapid;

    /**
     * 询价报告数
     */
    private Integer inquiry;

    /**
     * 咨询报告数
     */
    private Integer advisory;

    /**
     * 批量估价数
     */
    private Integer batch;

}
