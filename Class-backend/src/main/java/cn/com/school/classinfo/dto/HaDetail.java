package cn.com.school.classinfo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author dongpp
 * @date 2018/11/7
 */
@Data
public class HaDetail {
    private String id;
    private String streetCode;
    private String streetName;
    private String distCode;
    private String distName;
    private String name;
    private String haClCode;
    private String clCode;
    private Integer imageCount;
    private String imageUrl;
    private String isnew;
    private String newPriceTime;
    private Double newPrice;
    private String phasesTime;
    private String phasesName;
    private String incomeHouse;
    private String salePhases;
    private Double price;
    private Double priceRise;
    private Double leasePrice;
    private Double leasePriceRise;
    private String detail;
    private Integer pageView;
    private Integer saleSum;
    private Integer leaseSum;
    private String kpdate;
    private String buildyear;
    private String protoType;
    private String groundArea;
    private String groundAreaNum;
    private String constArea;
    private String devCoName;
    private String volumeRate;
    private String greeningRate;
    private String wyCoName;
    private String propertyCost;
    private String bldgType;
    private String brInfo;
    private String license;
    private String obLift;
    private String bldgstru;
    private String landuse;
    private String haClName;
    private String streetNo;
    private String obLevel;
    private String obAutomation;
    private String saleServiceTel;
    private List<CarItem> carItem;
    private List<Cos> cos;
    private String location;
    private String bdlocation;
}
