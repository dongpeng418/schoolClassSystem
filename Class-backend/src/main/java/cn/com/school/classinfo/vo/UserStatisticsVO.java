package cn.com.school.classinfo.vo;

import lombok.Data;

/**
 * 登录用户估价统计信息
 * @author dongpp
 * @date 2018/12/3
 */
@Data
public class UserStatisticsVO {

    private Integer rapidTotal;

    private Integer batchTotal;

    private Integer inquiryTotal;

    private Integer advisoryTotal;

}
