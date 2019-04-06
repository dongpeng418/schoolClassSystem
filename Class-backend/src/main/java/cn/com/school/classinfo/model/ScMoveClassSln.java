package cn.com.school.classinfo.model;

public class ScMoveClassSln {
    private Integer mcsId;

    private Integer sId;

    private String fcsSubjectName;

    private String fcsSubjectCode;

    public Integer getMcsId() {
        return mcsId;
    }

    public void setMcsId(Integer mcsId) {
        this.mcsId = mcsId;
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public String getFcsSubjectName() {
        return fcsSubjectName;
    }

    public void setFcsSubjectName(String fcsSubjectName) {
        this.fcsSubjectName = fcsSubjectName == null ? null : fcsSubjectName.trim();
    }

    public String getFcsSubjectCode() {
        return fcsSubjectCode;
    }

    public void setFcsSubjectCode(String fcsSubjectCode) {
        this.fcsSubjectCode = fcsSubjectCode == null ? null : fcsSubjectCode.trim();
    }
}