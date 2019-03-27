package cn.com.school.classinfo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author dongpp
 * @date 2018/11/24
 */
@Data
public class TenantEvalItemVO {


    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 登录名
     */
    private String loginUser;

    /**
     * 类型
     */
    private String evalMethod;

    /**
     * 类型
     */
    private String evalType;

    /**
     * 日期
     */
    private Date evalDate;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 地址
     */
    private String location;

    /**
     * 小区名称
     */
    private String haName;

    /**
     * 面积
     */
    private Double bldgArea;

    /**
     * 单价元
     */
    private Double unitPrice;

    /**
     * 总价万元
     */
    private Double totalPrice;
}
