package cn.com.school.classinfo.model;

import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ScTeacherInfo {

    private Integer tId;

    @Excel(name = "姓名")
    private String tName;

    @Excel(name = "性别")
    private String tSex;

    @Excel(name = "出生年月")
    private Date tBirthday;

    @Excel(name = "教龄")
    private Integer tTeachYear;//教龄

    @Excel(name = "职称")
    private String tCertification;//职称

    @Excel(name = "职工编号")
    private String tTeacherNo;//职工编号

    /**
     * @return the tId
     */
    public Integer gettId() {
        return tId;
    }

    /**
     * @param tId the tId to set
     */
    public void settId(Integer tId) {
        this.tId = tId;
    }

    /**
     * @return the tName
     */
    public String gettName() {
        return tName;
    }

    /**
     * @param tName the tName to set
     */
    public void settName(String tName) {
        this.tName = tName;
    }

    /**
     * @return the tSex
     */
    public String gettSex() {
        return tSex;
    }

    /**
     * @param tSex the tSex to set
     */
    public void settSex(String tSex) {
        this.tSex = tSex;
    }

    /**
     * @return the tBirthday
     */
    public Date gettBirthday() {
        return tBirthday;
    }

    /**
     * @param tBirthday the tBirthday to set
     */
    public void settBirthday(Date tBirthday) {
        this.tBirthday = tBirthday;
    }

    /**
     * @return the tTeachYear
     */
    public Integer gettTeachYear() {
        return tTeachYear;
    }

    /**
     * @param tTeachYear the tTeachYear to set
     */
    public void settTeachYear(Integer tTeachYear) {
        this.tTeachYear = tTeachYear;
    }

    /**
     * @return the tCertification
     */
    public String gettCertification() {
        return tCertification;
    }

    /**
     * @param tCertification the tCertification to set
     */
    public void settCertification(String tCertification) {
        this.tCertification = tCertification;
    }

    /**
     * @return the tTeacherNo
     */
    public String gettTeacherNo() {
        return tTeacherNo;
    }

    /**
     * @param tTeacherNo the tTeacherNo to set
     */
    public void settTeacherNo(String tTeacherNo) {
        this.tTeacherNo = tTeacherNo;
    }



}