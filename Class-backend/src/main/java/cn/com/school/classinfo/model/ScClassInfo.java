package cn.com.school.classinfo.model;

public class ScClassInfo {
    private Integer cId;

    private String cClassName;

    private Integer stuId;

    private Integer sId;

    private Integer cType;

    private Integer cSlnId;

    private String cClassCode;

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getcClassName() {
        return cClassName;
    }

    public void setcClassName(String cClassName) {
        this.cClassName = cClassName == null ? null : cClassName.trim();
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public Integer getcType() {
        return cType;
    }

    public void setcType(Integer cType) {
        this.cType = cType;
    }

    public Integer getcSlnId() {
        return cSlnId;
    }

    public void setcSlnId(Integer cSlnId) {
        this.cSlnId = cSlnId;
    }

    public String getcClassCode() {
        return cClassCode;
    }

    public void setcClassCode(String cClassCode) {
        this.cClassCode = cClassCode == null ? null : cClassCode.trim();
    }
}