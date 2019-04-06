package cn.com.school.classinfo.model;

import java.util.Date;

public class ScTeacherInfo {
    private Integer tId;

    private String tName;

    private String tSex;

    private Date tBirthday;

    private Integer tTeachYear;

    private String tCertification;

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName == null ? null : tName.trim();
    }

    public String gettSex() {
        return tSex;
    }

    public void settSex(String tSex) {
        this.tSex = tSex == null ? null : tSex.trim();
    }

    public Date gettBirthday() {
        return tBirthday;
    }

    public void settBirthday(Date tBirthday) {
        this.tBirthday = tBirthday;
    }

    public Integer gettTeachYear() {
        return tTeachYear;
    }

    public void settTeachYear(Integer tTeachYear) {
        this.tTeachYear = tTeachYear;
    }

    public String gettCertification() {
        return tCertification;
    }

    public void settCertification(String tCertification) {
        this.tCertification = tCertification == null ? null : tCertification.trim();
    }
}