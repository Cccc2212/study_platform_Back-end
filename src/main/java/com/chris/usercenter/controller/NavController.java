package com.chris.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chris.usercenter.commom.BaseResponse;
import com.chris.usercenter.commom.ErrorCode;
import com.chris.usercenter.commom.ResultUtils;
import com.chris.usercenter.exception.BusinessException;
import com.chris.usercenter.model.domain.Nav;
import com.chris.usercenter.model.domain.User;
import com.chris.usercenter.model.domain.request.NavCreateRequest;
import com.chris.usercenter.model.domain.request.NavDeleteRequest;
import com.chris.usercenter.model.domain.request.NavUpdateRequest;
import com.chris.usercenter.service.NavService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.chris.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.chris.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 导航接口
 *
 * @author Chris
 */


@RestController
@RequestMapping("/user")
public class NavController {
    @Resource
    private NavService navService;

    /**
     * 查询导航
     *
     * @param request
     * @return
     */
    @GetMapping("/search_nav")
    public BaseResponse<List<Nav>> searchNavs(HttpServletRequest request) {
        //查询
        QueryWrapper<Nav> queryWrapper = new QueryWrapper<>();
        List<Nav> navList = navService.list(queryWrapper);
        return ResultUtils.success(navList);
    }

    /**
     * 删除导航(须鉴权)
     *
     * @param navDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete_nav")
    public BaseResponse<Boolean> deleteNav(@RequestBody NavDeleteRequest navDeleteRequest, HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限删除");
        }
        // 检查 navKey 是否为空
        if (StringUtils.isAnyBlank(navDeleteRequest.getNavKey())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "navKey无效");
        }
        // 使用 LambdaQueryWrapper 通过 navKey 删除导航
        QueryWrapper<Nav> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("navKey", navDeleteRequest.getNavKey());
        boolean result = navService.remove(queryWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除导航失败");
        }
        return ResultUtils.success(result);
    }

    /**
     * 新增导航
     *
     * @param navCreateRequest
     * @param request
     * @return
     */
    @PostMapping("/create_nav")
    public BaseResponse<Boolean> createNav(@RequestBody NavCreateRequest navCreateRequest,HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 仅管理员可操作
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限操作");
        }
        //@RequestBody可以让springmvc知道怎么把前端的json参数和后面的对象做关联
        if (navCreateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String label = navCreateRequest.getLabel();
        String navKey = navCreateRequest.getNavKey();
        String url = navCreateRequest.getUrl();
        if (StringUtils.isAnyBlank(label, navKey, url)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "必填参数缺失");
        }
        boolean result = navService.createNav(label,navKey,url);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建导航失败");
        }
        return ResultUtils.success(result);
    }

    /**
     * 更新导航
     *
     * @param navUpdateRequest
     * @return
     */
    @PostMapping("/update_nav")
    public BaseResponse<Boolean> updateNav(@RequestBody NavUpdateRequest navUpdateRequest) {
        // 校验请求体是否为空
        if (navUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取参数
        Long id = navUpdateRequest.getId();
        String label = navUpdateRequest.getLabel();
        String navKey = navUpdateRequest.getNavKey();
        String url = navUpdateRequest.getUrl();
        // 执行更新操作
        boolean result = navService.updateNav(id,label,navKey,url);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, result + "更新导航失败1");
        }
        return ResultUtils.success(result);
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

