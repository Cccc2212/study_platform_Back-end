package com.chris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chris.usercenter.commom.ErrorCode;
import com.chris.usercenter.exception.BusinessException;
import com.chris.usercenter.mapper.SudokupuzzlesMapper;
import com.chris.usercenter.model.domain.Sudokupuzzles;
import com.chris.usercenter.service.SudokupuzzlesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
* @author user
* @description 针对表【sudokupuzzles】的数据库操作Service实现
* @createDate 2025-02-18 14:29:09
*/
@Service
@Slf4j
public class SudokupuzzlesServiceImpl extends ServiceImpl<SudokupuzzlesMapper, Sudokupuzzles>
    implements SudokupuzzlesService {
    /**
     * 查询随机题目
     *
     */
    @Override
    public Sudokupuzzles getRandomPuzzle() {
        List<Sudokupuzzles> puzzles = baseMapper.selectList(null);  // 获取所有数独题目

        // 随机选择一个
        Random rand = new Random();
        int randomIndex = rand.nextInt(puzzles.size());
        return puzzles.get(randomIndex);  // 返回随机选择的题目
    }

    /**
     * 通过题目难度来查询随机题目
     *
     */
    @Override
    public Sudokupuzzles getRandomPuzzleByDifficulty(Integer difficulty) {
        QueryWrapper<Sudokupuzzles> queryWrapper = new QueryWrapper<>();
        if (difficulty != null) {
            queryWrapper.eq("difficulty", difficulty);
        }
        List<Sudokupuzzles> puzzles = baseMapper.selectList(queryWrapper);
        if (puzzles == null || puzzles.isEmpty()) {
            throw new RuntimeException(difficulty == null ?
                    "没有可用的数独谜题" : "难度 " + difficulty + " 的数独谜题不存在");
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(puzzles.size());
        return puzzles.get(randomIndex);
    }
    /**
     * 保存一个数独题目到数据库
     *
     * @return
     */
    @Override
    public Integer savePuzzle(String initial_board,String solution,Integer difficulty){
        Sudokupuzzles sudokupuzzles = new Sudokupuzzles();
        sudokupuzzles.setDifficulty(difficulty);
        sudokupuzzles.setSolution(solution);
        sudokupuzzles.setInitial_board(initial_board);
        boolean saveResult = this.save(sudokupuzzles);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "保存数据异常:(");
        }
        return sudokupuzzles.getId();
    }
}




