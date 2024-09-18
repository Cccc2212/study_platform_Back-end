package com.chris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chris.usercenter.commom.ErrorCode;
import com.chris.usercenter.exception.BusinessException;
import com.chris.usercenter.mapper.NavMapper;
import com.chris.usercenter.model.domain.Nav;
import com.chris.usercenter.service.NavService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 导航页实现类
 *
 * @author user
 * @description 针对表【nav(导航表)】的数据库操作Service实现
 * @createDate 2024-09-14 18:15:58
 */
@Service
@Slf4j
public class NavServiceImpl extends ServiceImpl<NavMapper, Nav>
        implements NavService {
    @Resource
    private NavMapper navMapper;

    /**
     * 创建导航
     *
     * @param label
     * @param navKey
     * @param url
     * @return
     */
    @Override
    public boolean createNav(String label, String navKey, String url) {
        // 校验
        if (StringUtils.isAnyBlank(label, navKey, url)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入参数为空");
        }
        // navKey不能重复(唯一值)
        QueryWrapper<Nav> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("navKey", navKey);
        long count = navMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "navKey重复了:(");
        }
        //插入数据
        Nav nav = new Nav();
        nav.setLabel(label);
        nav.setNavKey(navKey);
        nav.setUrl(url);
        boolean saveResult = this.save(nav);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "保存数据异常:(");
        }
        return saveResult;
    }

    @Override
    public boolean updateNav(Long id,String label, String navKey, String url) {
        // 校验
        if (StringUtils.isAnyBlank(label, navKey, url)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入参数为空");
        }
        //  更新导航信息
        Nav updatedNav = new Nav();
        updatedNav.setId(id);
        updatedNav.setLabel(label);
        updatedNav.setNavKey(navKey);
        updatedNav.setUrl(url);
        // 执行更新操作
        boolean result = this.updateById(updatedNav);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新导航失败");
        }
        return result;
    }
}




