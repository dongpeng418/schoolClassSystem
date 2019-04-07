package cn.com.school.classinfo.model;

import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ScStudentInfo {

    private Integer stuId;

    @Excel(name = "学号")
    private String stuNo;

    @Excel(name = "性别")
    private String stuSex;

    @Excel(name = "联系方式")
    private String stuPhone;

    private Integer stuPubId;

    @Excel(name = "所属行政班")
    private String stuPubName;//行政班

    @Excel(name = "姓名")
    private String stuName;

    @Excel(name = "出生年月")
    private Date stuBirthday;

}