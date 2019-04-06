package cn.com.school.classinfo.model;

public class ScBaseDictionary {
    private Integer bdId;

    private String bdName;

    private String bdType;

    private String bdValue;

    public Integer getBdId() {
        return bdId;
    }

    public void setBdId(Integer bdId) {
        this.bdId = bdId;
    }

    public String getBdName() {
        return bdName;
    }

    public void setBdName(String bdName) {
        this.bdName = bdName == null ? null : bdName.trim();
    }

    public String getBdType() {
        return bdType;
    }

    public void setBdType(String bdType) {
        this.bdType = bdType == null ? null : bdType.trim();
    }

    public String getBdValue() {
        return bdValue;
    }

    public void setBdValue(String bdValue) {
        this.bdValue = bdValue == null ? null : bdValue.trim();
    }
}