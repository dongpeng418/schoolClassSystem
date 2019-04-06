package cn.com.school.classinfo.model;

import java.util.Date;

public class ScStudentInfo {
    private Integer stuId;

    private String stuNo;

    private String stuNum;

    private String stuSex;

    private Date stu;

    private String stuPhone;

    private Integer stuPubId;

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo == null ? null : stuNo.trim();
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum == null ? null : stuNum.trim();
    }

    public String getStuSex() {
        return stuSex;
    }

    public void setStuSex(String stuSex) {
        this.stuSex = stuSex == null ? null : stuSex.trim();
    }

    public Date getStu() {
        return stu;
    }

    public void setStu(Date stu) {
        this.stu = stu;
    }

    public String getStuPhone() {
        return stuPhone;
    }

    public void setStuPhone(String stuPhone) {
        this.stuPhone = stuPhone == null ? null : stuPhone.trim();
    }

    public Integer getStuPubId() {
        return stuPubId;
    }

    public void setStuPubId(Integer stuPubId) {
        this.stuPubId = stuPubId;
    }
}