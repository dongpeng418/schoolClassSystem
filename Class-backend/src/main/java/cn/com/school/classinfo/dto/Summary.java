package cn.com.school.classinfo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author dongpp
 * @date 2018/10/24
 */
@Data
public class Summary {
    private Double totalPrice;
    private Double unitPrice;
    private Double yieldr;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh_CN", timezone = "GMT+8")
    private Date evalTime;
    private Double unitPriceMax;
    private Double unitPriceMin;
    private Double unitPriceAVG;
    private Double priceStd;
    private Double distance;
    private Integer haCount;
    private Integer count;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh_CN", timezone = "GMT+8")
    private Date sampleBegin;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh_CN", timezone = "GMT+8")
    private Date sampleEnd;
}
