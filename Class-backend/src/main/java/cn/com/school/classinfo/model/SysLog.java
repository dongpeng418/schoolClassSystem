package cn.com.school.classinfo.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysLog {

    private Integer id;

    private Integer tenantId;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作人帐号
     */
    private String operateBy;

    /**
     * 操作人姓名
     */
    private String operateName;


    /**
     * 操作内容
     */
    private String operateLog;

}