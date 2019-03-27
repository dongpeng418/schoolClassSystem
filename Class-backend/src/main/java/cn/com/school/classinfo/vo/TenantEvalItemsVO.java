package cn.com.school.classinfo.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * @author dongpp
 * @date 2018/11/24
 */
@Data
public class TenantEvalItemsVO {

    /**
     * 客户总数
     */
    private Integer customerTotal;

    /**
     * 帐号总数
     */
    private Integer userTotal;

    /**
     * 房产总数
     */
    private Integer haTotal;

    /**
     * 快速估价总数
     */
    private Integer rapidTotal;

    /**
     * 批量估价总数
     */
    private Integer batchTotal;

    /**
     * 询价报告总数
     */
    private Integer inquiryTotal;

    /**
     * 咨询报告总数
     */
    private Integer advisoryTotal;

    /**
     * 明细列表
     */
    private PageInfo<TenantEvalItemVO> pageInfo;

}
