package com.chris.usercenter.controller;

import com.chris.usercenter.commom.BaseResponse;
import com.chris.usercenter.commom.ErrorCode;
import com.chris.usercenter.commom.ResultUtils;
import com.chris.usercenter.exception.BusinessException;
import com.chris.usercenter.model.domain.Sudokupuzzles;
import com.chris.usercenter.model.domain.User;
import com.chris.usercenter.model.domain.request.PuzzleGenerateRequest;
import com.chris.usercenter.service.SudokupuzzlesService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.chris.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.chris.usercenter.constant.UserConstant.USER_LOGIN_STATE;

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
    /**
     * 保存数独题目
     * @param puzzleGenerateRequest
     *
     * @return
     */
    @PostMapping("/savePuzzle")
    public BaseResponse<Integer> savePuzzle(@RequestBody PuzzleGenerateRequest puzzleGenerateRequest, HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 仅管理员可操作
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限操作");
        }
        //@RequestBody可以让springmvc知道怎么把前端的json参数和后面的对象做关联
        if (puzzleGenerateRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String initial_board = puzzleGenerateRequest.getInitial_board();
        Integer difficulty = puzzleGenerateRequest.getDifficulty();
        String solution = puzzleGenerateRequest.getSolution();
        if (StringUtils.isAnyBlank(initial_board, solution)) {
            return null;
        }
        Integer result = sudokupuzzlesService.savePuzzle(initial_board, solution, difficulty);
        return ResultUtils.success(result);
    }
    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
