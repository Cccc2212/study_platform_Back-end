package com.chris.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.usercenter.model.domain.Nav;

/**
 * 导航页Service
 *
* @author user
* @description 针对表【nav(导航表)】的数据库操作Service
* @createDate 2024-09-14 18:15:58
*/
public interface NavService extends IService<Nav> {
    /**
     *
     * @param label
     * @param navKey
     * @param url
     * @return
     */
    boolean createNav(String label,String navKey,String url);
    boolean updateNav(Long id, String label,String navKey,String url);

}
