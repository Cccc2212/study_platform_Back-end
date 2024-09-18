package com.chris.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.usercenter.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户服务Service
 *
 * @author chris
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-09-04 21:40:15
 */


public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 检验密码
     * @param platformCode  平台编号
     * @return 新用户ID
     */
    long userRegister(String username,String userAccount, String userPassword, String checkPassword,String platformCode);
//    long userdelete(String userAccount);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏(隐藏敏感信息）
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 管理员更新用户信息
     *
     * @param userId
     * @param username
     * @param userAccount
     * @param avatarUrl
     * @param gender
     * @param phone
     * @param email
     * @param userStatus
     * @param platformCode
     * @param userRole
     * @return
     */
    boolean updateUser(Long userId, String username, String userAccount, String avatarUrl, Integer gender,
                       String phone, String email, Integer userStatus, String platformCode,
                       Integer userRole);
}
