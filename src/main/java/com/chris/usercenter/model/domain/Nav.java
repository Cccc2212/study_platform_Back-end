package com.chris.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 导航表
 * @TableName nav
 */
@TableName(value ="nav")
@Data
public class Nav implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名
     */
    private String label;

    /**
     * key值
     */
    private String navKey;

    /**
     * 网址
     */
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}