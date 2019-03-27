package cn.com.school.classinfo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author dongpp
 * @date 2018/11/7
 */
@Data
public class Analysis {
    private Integer haCount;
    private String haUnit;
    private List<AnalysisItem> rows;
}
