package cn.com.school.classinfo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author dongpp
 * @date 2018/10/24
 */
@Data
public class House {
    private String id;
    private CommonInfo haInfo;
    private CommonInfo distInfo;
    private CommonInfo streetInfo;
    private String streetNo;
    private Double unitPrice;
    private String unitOfUnitPrice;
    private Double totalPrice;
    private String unitOfTotalPrice;
    private Double bldgArea;
    private Integer floor;
    private Integer bheight;
    private CommonInfo face;
    private CommonInfo intDeco;
    private CommonInfo propType;
    private Integer br;
    private Integer lr;
    private Integer cr;
    private Double ba;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh_CN", timezone = "GMT+8")
    private Date offerTime;
}
