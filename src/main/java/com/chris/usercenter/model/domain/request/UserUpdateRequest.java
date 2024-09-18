package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户更新数据请求体
 *
 * @author Chris
 */
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 4932147411870850172L;
    private Long id;
    private String username;
    private String userAccount;
    private String avatarUrl;
    private Integer gender;
    private String phone;
    private String email;
    private Integer userStatus;
    private String platformCode;
    private Integer userRole;
    private Date updateTime;

}
