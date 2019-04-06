package cn.com.school.classinfo.model;

public class ScSubjectListPlan {
    private Integer plsId;

    private Integer cId;

    private Integer plsWeekNum;

    private Integer plsNum;

    private String plsName;

    private Integer tId;

    private Integer plsTermNum;

    public Integer getPlsId() {
        return plsId;
    }

    public void setPlsId(Integer plsId) {
        this.plsId = plsId;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public Integer getPlsWeekNum() {
        return plsWeekNum;
    }

    public void setPlsWeekNum(Integer plsWeekNum) {
        this.plsWeekNum = plsWeekNum;
    }

    public Integer getPlsNum() {
        return plsNum;
    }

    public void setPlsNum(Integer plsNum) {
        this.plsNum = plsNum;
    }

    public String getPlsName() {
        return plsName;
    }

    public void setPlsName(String plsName) {
        this.plsName = plsName == null ? null : plsName.trim();
    }

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public Integer getPlsTermNum() {
        return plsTermNum;
    }

    public void setPlsTermNum(Integer plsTermNum) {
        this.plsTermNum = plsTermNum;
    }
}