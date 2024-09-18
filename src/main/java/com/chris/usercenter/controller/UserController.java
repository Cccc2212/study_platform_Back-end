package com.chris.usercenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chris.usercenter.commom.BaseResponse;
import com.chris.usercenter.commom.ErrorCode;
import com.chris.usercenter.commom.ResultUtils;
import com.chris.usercenter.exception.BusinessException;
import com.chris.usercenter.model.domain.User;
import com.chris.usercenter.model.domain.request.UserDeleteRequest;
import com.chris.usercenter.model.domain.request.UserLoginRequest;
import com.chris.usercenter.model.domain.request.UserRegisterRequest;
import com.chris.usercenter.model.domain.request.UserUpdateRequest;
import com.chris.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.chris.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.chris.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author Chris
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        // 校验请求体是否为空
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取参数
        Long userId = userUpdateRequest.getId();
        String username = userUpdateRequest.getUsername();
        String userAccount = userUpdateRequest.getUserAccount();
        String avatarUrl = userUpdateRequest.getAvatarUrl();
        Integer gender = userUpdateRequest.getGender();
        String phone = userUpdateRequest.getPhone();
        String email = userUpdateRequest.getEmail();
        Integer userStatus = userUpdateRequest.getUserStatus();
        String platformCode = userUpdateRequest.getPlatformCode();
        Integer userRole = userUpdateRequest.getUserRole();
        // 执行更新操作
        boolean result = userService.updateUser(userId, username, userAccount, avatarUrl, gender, phone, email, userStatus, platformCode, userRole);
        // 返回更新结果
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, result + "更新失败");
        }
        return ResultUtils.success(result);
    }

    /**
     * 注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        //@RequestBody可以让springmvc知道怎么把前端的json参数和后面的对象做关联
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String platformCode = userRegisterRequest.getPlatformCode();
        if (StringUtils.isAnyBlank(username, userAccount, userPassword, checkPassword, platformCode)) {
            return null;
        }
        long result = userService.userRegister(username, userAccount, userPassword, checkPassword, platformCode);
        return ResultUtils.success(result);
    }

    /**
     * 删除用户(须鉴权)
     *
     * @param userDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
//        String userAccount = currentUser.getUserAccount();
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限删除");
        }
        // 检查 userAccount 是否为空
        if (StringUtils.isAnyBlank(userDeleteRequest.getUserAccount())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户无效");
        }
        // 检查是否尝试删除自己
        if (currentUser.getUserAccount() != null && currentUser.getUserAccount().equals(userDeleteRequest.getUserAccount())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能删除自己");
        }
        // 使用 LambdaQueryWrapper 通过 userAccount 删除用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userDeleteRequest.getUserAccount());
        boolean result = userService.remove(queryWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除用户失败");
        }
        return ResultUtils.success(result);
    }

    /**
     * 登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        //@RequestBody可以让springmvc知道怎么把前端的json参数和后面的对象做关联
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        //@RequestBody可以让springmvc知道怎么把前端的json参数和后面的对象做关联
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 防止重要数据泄露
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 管理员查询用户(须鉴权)
     *
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        //仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        //查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
            //default是前后都有通配符的模糊查询
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        //userList转化为数据流，再遍历里面的每个元素，再把每一个里面的password变为空
        return ResultUtils.success(list);
    }

    /**
     * 普通用户查询用户(无须鉴权)
     *
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/searchcommon")
    public BaseResponse<List<User>> searchUsersCommon(String username, HttpServletRequest request) {
        //查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
            //default是前后都有通配符的模糊查询
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        //userList转化为数据流，再遍历里面的每个元素，再把每一个里面的password变为空
        return ResultUtils.success(list);
    }


    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
