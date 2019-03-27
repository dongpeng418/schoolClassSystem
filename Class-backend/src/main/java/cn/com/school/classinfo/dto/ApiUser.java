package cn.com.school.classinfo.dto;

import lombok.Data;

/**
 * 接口用户
 * @author dongpp
 * @date 2018/10/26
 */
@Data
public class ApiUser {

    private String userToken;

    private String unionUid;

    private String nickName;

}
