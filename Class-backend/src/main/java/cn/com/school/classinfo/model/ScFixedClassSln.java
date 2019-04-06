package cn.com.school.classinfo.model;

public class ScFixedClassSln {
    private Integer fcsId;

    private Integer sId;

    private String fcsSubjectName;

    private String fcsSubjectCode;

    public Integer getFcsId() {
        return fcsId;
    }

    public void setFcsId(Integer fcsId) {
        this.fcsId = fcsId;
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