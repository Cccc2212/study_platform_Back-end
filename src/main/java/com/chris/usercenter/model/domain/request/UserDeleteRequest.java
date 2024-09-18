package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户删除请求体
 *
 * @author Chris
 */
@Data
public class UserDeleteRequest implements Serializable {
    //序列化接口实现
    private static final long serialVersionUID = -7996368013566231585L;

    private String userAccount;
//    private String userPassword;
//    private String checkPassword;
//    private  String platformCode;
}