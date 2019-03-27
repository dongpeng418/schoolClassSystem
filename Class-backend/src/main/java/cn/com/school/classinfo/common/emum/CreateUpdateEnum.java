package cn.com.school.classinfo.common.emum;

public enum CreateUpdateEnum {
    UPDATE("update"),
    CREATE("create");


    private String message;

    CreateUpdateEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
