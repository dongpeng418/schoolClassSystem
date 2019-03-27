package cn.com.school.classinfo.dto.filter;

import lombok.Data;

/**
 * @author dongpp
 * @date 2018/10/30
 */
@Data
public class FilterHa {
    private String cityName;
    private String cityCode;
    private String distName;
    private String distCode;
    private String haName;
    private String haCode;
    private Double lon;
    private Double lat;
    private String haClCode;
    private String propTypeName;
    private String propTypeCode;
    private String bldgTypeCode;
    private String buildYear;
    private String bldgArea;
    private String mainStreet;
    private String haPropType;
}
