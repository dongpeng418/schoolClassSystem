package cn.com.school.classinfo.common.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 估价字段查询条件
 *
 * @author dongpp
 * @date 2018/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EvalFieldQuery extends BaseQuery {

    private Integer companyId;

    private Integer type;

}
