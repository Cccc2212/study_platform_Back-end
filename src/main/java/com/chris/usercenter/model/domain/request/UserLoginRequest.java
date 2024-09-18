package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author Chris
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -1598183918564036328L;
    private String userAccount;
    private String userPassword;
}
