package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 导航创建请求体
 *
 * @author Chris
 */
@Data
public class NavCreateRequest implements Serializable {
    //序列化接口实现
    private static final long serialVersionUID = 9056932556663943335L;

    private String label;
    private String navKey;
    private String url;

}
