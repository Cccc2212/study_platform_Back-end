package com.chris.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import  com.chris.usercenter.model.domain.Sudokupuzzles;

/**
 * 数独题目Service
 *
* @author user
* @description 针对表【sudokupuzzles】的数据库操作Service
* @createDate 2025-02-18 14:29:09
*/
public interface SudokupuzzlesService extends IService<Sudokupuzzles> {

    /**
     *获得一个随机数独题目
     *
     * @return
     */
    Sudokupuzzles getRandomPuzzle();
    /**
     *获得一个指定难度的数独题目
     *
     * @return
     */
    Sudokupuzzles getRandomPuzzleByDifficulty(Integer difficulty);
}

