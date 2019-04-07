package cn.com.school.classinfo.model;

import java.util.Date;

import lombok.Data;

@Data
public class ScUser {
    private Integer id;

    private Integer userType;

    private Integer userId;

    private String loginUser;

    private String password;

    //明文密码
    private String enablePassword;

    private String createdId;

    private Date createdTime;

    private Date lastLoginDate;

    private Date currentLoginDate;

}