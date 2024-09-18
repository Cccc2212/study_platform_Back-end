package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author Chris
 */
@Data
public class UserRegisterRequest implements Serializable {
    //序列化接口实现
    private static final long serialVersionUID = 6857033905097791339L;

    private String username;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private  String platformCode;
}
