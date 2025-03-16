package com.chris.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;
/**
 * 数独题目删除请求体
 *
 * @author Chris
 */
@Data
public class PuzzleDeleteRequest implements Serializable {
    private static final long serialVersionUID = 6695574425912887894L;
    private  Integer id;
}
