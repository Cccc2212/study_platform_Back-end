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
     * @return 新用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);
}
