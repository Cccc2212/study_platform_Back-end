package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;
/**
 * 导航删除请求体
 *
 * @author Chris
 */
@Data
public class NavDeleteRequest implements Serializable {
    private static final long serialVersionUID = 4330422299782588514L;
    private String navKey;
}
