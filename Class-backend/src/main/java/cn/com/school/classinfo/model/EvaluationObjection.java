package cn.com.school.classinfo.model;


import lombok.Data;

import java.util.Date;

@Data
public class EvaluationObjection {

    /**
     * id
     */
    private Integer id;

    private Integer tenantId;

    private Integer companyId;

    private Integer organizationId;

    /**
     * 估价ID
     */
    private Integer evalId;

    /**
     * 估价类型，1：售价，2：租金
     */
    private Integer evalType;

    /**
     * 估价唯一码
     */
    private String evalCode;

    /**
     * 状态，1：待处理，2：已处理
     */
    private Integer state;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 小区名称
     */
    private String haName;

    /**
     * 估价结果（评估总价）
     */
    private Double totalPrice;

    /**
     * 面积
     */
    private Double bldgArea;

    /**
     * 评估日期
     */
    private Date evalDate;

    /**
     * 实际价格
     */
    private Double actualPrice;

    /**
     * 异议时间
     */
    private Date objectionTime;

    /**
     * 异议原因
     */
    private String objectionReason;

    /**
     * 异议答复
     */
    private String reply;

    private Date createTime;
    private String createBy;
    private String createName;
    private Date updateTime;
    private String updateBy;
    private String updateName;


}
