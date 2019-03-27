package cn.com.school.classinfo.dto;

import lombok.Data;
import lombok.ToString;

/**
 * GPS信息
 * 
 * @author hanpt
 *
 */
@Data
@ToString
public class GpsInfo {
    private String coord;
    private String coordType;
}
