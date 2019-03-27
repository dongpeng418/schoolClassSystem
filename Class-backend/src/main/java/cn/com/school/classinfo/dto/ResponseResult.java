package cn.com.school.classinfo.dto;

import lombok.Data;

/**
 * 估价返回结果对象
 * @author dongpp
 * @date 2018/10/24
 */
@Data
public class ResponseResult {
    private HouseInfo term;
    private AssessResult assessResult;
}
