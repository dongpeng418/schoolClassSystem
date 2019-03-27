package cn.com.school.classinfo.vo;

import lombok.Data;

/**
 * @author dongpp
 * @date 2018/11/23
 */
@Data
public class EvalSummaryItemVO {

    /**
     * 机构ID
     */
    private Integer organizationId;

    /**
     * 机构名称
     */
    private String organizationName;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户登录名
     */
    private String loginUser;

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
