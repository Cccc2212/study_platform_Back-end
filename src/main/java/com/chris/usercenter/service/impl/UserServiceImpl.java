package com.chris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chris.usercenter.commom.ErrorCode;
import com.chris.usercenter.exception.BusinessException;
import com.chris.usercenter.mapper.UserMapper;
import com.chris.usercenter.model.domain.User;
import com.chris.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chris.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
 * @author chris
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-09-04 21:40:15
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值定了盐值定了盐值定了盐值定了盐值定了盐值定了盐值定了盐值定了（混淆密码
     */
    private static final String SALT = "chris";
//    //用户删除功能(已移至controller)
//    long userdelete(String userAccount){
//        // 查询用户是否存在
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("userAccount", userAccount);
//        User user = userMapper.selectOne(queryWrapper);
//        //检验是否存在
//        if (user == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在:(");
//        }
//        // 删除用户
//        boolean removeResult = this.removeById(user.getId());
//        if (!removeResult) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"删除用户失败:(");
//        }
//        return user.getId();
//    }

    /**
     * 用户注册功能
     *
     * @param username
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 检验密码
     * @param platformCode  平台编号
     * @return
     */
    @Override
    public long userRegister(String username, String userAccount, String userPassword, String checkPassword, String platformCode) {
        // 1.校验
        if (StringUtils.isAnyBlank(username, userAccount, userPassword, checkPassword, platformCode)) {
            //  修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短，小于四位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短，小于八位");
        }
        if (platformCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "平台编号过长，大于五位");
        }
        // 账户不包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符:(");
        }
        // 校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致:(");
        }
        // 账户不能重复(唯一账户)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复了:(");
        }
        // 平台编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("platformCode", platformCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "平台编号重复了:(");
        }
        // 2.加密密码（不能以明文形式存储到数据库）
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据
        User user = new User();
        user.setUsername(username);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlatformCode(platformCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "保存数据异常:(");
        }
        return user.getId();
    }

    /**
     * 用户登录功能
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入参数为空:(");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短，小于四位:(");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短，小于八位:(");
        }
        // 账户不包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符:(");
        }
        // 2.加密密码（不能以明文形式存储到数据库）
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("用户不存在或密码错误:(");
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在或密码错误:(");
        }
        //3.用户脱敏(隐藏敏感信息）
        User safetyUser = getSafetyUser(user);
        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏(隐藏敏感信息）
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPlatformCode(originUser.getPlatformCode());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        return safetyUser;
    }

    /**
     * 用户注销功能
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 管理员更新用户信息
     *
     * @param userId       用户ID
     * @param username     用户名
     * @param userAccount  用户账户
     * @param avatarUrl    用户头像
     * @param gender       性别
     * @param phone        电话
     * @param email        邮箱
     * @param userStatus   用户状态
     * @param platformCode 平台编号
     * @param userRole     用户角色
     * @return 更新是否成功
     */
    @Override
    public boolean updateUser(Long userId, String username, String userAccount, String avatarUrl, Integer gender,
                              String phone, String email, Integer userStatus, String platformCode,
                              Integer userRole) {
        // 1.校验
        if (StringUtils.isAnyBlank(username, userAccount, platformCode)) {
            //  修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "有必填参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短，小于四位");
        }
        if (platformCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "平台编号过长，大于五位");
        }
        // 账户不包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符:(");
        }
        // 账户不能重复(唯一账户)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复了:(");
        }
        // 平台编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("platformCode", platformCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "平台编号重复了:(");
        }
        //  更新用户信息
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername(username);
        updatedUser.setUserAccount(userAccount);
        updatedUser.setAvatarUrl(avatarUrl);
        updatedUser.setGender(gender);
        updatedUser.setPhone(phone);
        updatedUser.setEmail(email);
        updatedUser.setUserStatus(userStatus);
        updatedUser.setPlatformCode(platformCode);
        updatedUser.setUserRole(userRole);
        // 执行更新操作
        boolean result = this.updateById(updatedUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新用户失败");
        }
        return true;
    }
}




