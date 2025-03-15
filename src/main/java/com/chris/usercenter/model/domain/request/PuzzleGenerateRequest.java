package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 *  数独生成请求体
 *
 * @author Chris
 */
@Data
public class PuzzleGenerateRequest implements Serializable {
    //序列化接口实现
    private static final long serialVersionUID = 5671810543885858507L;

    private String initial_board;
    private String solution;
    private Integer difficulty;

}
