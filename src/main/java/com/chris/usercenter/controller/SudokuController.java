package com.chris.usercenter.controller;

import com.chris.usercenter.commom.BaseResponse;
import com.chris.usercenter.model.domain.Sudokupuzzles;
import com.chris.usercenter.service.SudokupuzzlesService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 *
 * @author Chris
 */
@RestController
@RequestMapping("/sudoku")
public class SudokuController {

    @Resource
    private SudokupuzzlesService sudokupuzzlesService;
    /**
     * 获取一个随机数独题目
     *
     * @return 随机数独题目数据
     */
    @GetMapping("/getRandomPuzzle")  // 定义 GET 请求的映射路径
    public Sudokupuzzles getRandomPuzzle() {
        // 调用 Service 获取随机数独题目
        return sudokupuzzlesService.getRandomPuzzle(); // 返回获取到的数独题目
    }

    /**
     * 根据难度获取随机数独题目
     *
     * @param difficulty 可选的难度等级（1-4），未提供时返回任意难度
     * @return 随机数独题目数据
     */
    @GetMapping("/getRandomPuzzleByDifficulty")
    public BaseResponse<Sudokupuzzles> getRandomPuzzleByDifficulty(
            @RequestParam(value = "difficulty", required = false) Integer difficulty) {
        try {
            Sudokupuzzles puzzle = sudokupuzzlesService.getRandomPuzzleByDifficulty(difficulty);
            return new BaseResponse<>(200, puzzle, "成功");
        } catch (Exception e) {
            return new BaseResponse<>(500, null, "获取数独题目失败");
        }
    }
}
