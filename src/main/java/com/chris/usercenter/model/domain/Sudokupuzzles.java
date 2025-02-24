package com.chris.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 数独表
 * @TableName sudokupuzzles
 */
@TableName(value ="sudokupuzzles")
@Data
public class Sudokupuzzles implements Serializable {
    /**
     *
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 数独问题
     */
    private String initial_board;

    /**
     * 数独答案
     */
    private String solution;

    /**
     * 难度
     */
    private Integer difficulty;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}