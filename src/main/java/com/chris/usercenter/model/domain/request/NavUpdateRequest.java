package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 导航更新请求体
 *
 * @author Chris
 */
@Data
public class NavUpdateRequest implements Serializable {
    private static final long serialVersionUID = -6544734098257266651L;
    //序列化接口实现
    private Long id;
    private String label;
    private String navKey;
    private String url;

}