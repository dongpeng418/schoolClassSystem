package cn.com.school.classinfo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author dongpp
 * @date 2018/10/24
 */
@Data
public class AssessResult {

    private String logId;
    private String method;
    private String applyno;
    private String manremark;
    private String manflag;
    private Integer readflag;
    private Summary summary;
    private List<Sample> samples;
    private List<Beta> betas;
}
