package com.todo.todomanagementsystem.controller;

import com.todo.todomanagementsystem.vo.BorrowTrendVO;
import com.todo.todomanagementsystem.vo.CategoryRatioVO;
import com.todo.todomanagementsystem.vo.HotBookVO;
import com.todo.todomanagementsystem.vo.DashboardVO;
import com.todo.todomanagementsystem.service.StatService;
import com.todo.todomanagementsystem.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stat")
@Tag(name = "统计接口", description = "数据看板相关接口")
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping("/borrow-trend")
    @Operation(summary = "借阅趋势", description = "获取指定天数内的借阅趋势数据")
    public Result<List<BorrowTrendVO>> getBorrowTrend(
            @Parameter(description = "天数，默认7天") @RequestParam(required = false) Integer days) {
        return statService.getBorrowTrend(days);
    }

    @GetMapping("/category-ratio")
    @Operation(summary = "分类占比", description = "获取图书分类占比统计")
    public Result<List<CategoryRatioVO>> getCategoryRatio() {
        return statService.getCategoryRatio();
    }

    @GetMapping("/hot-books")
    @Operation(summary = "热门图书", description = "获取借阅次数最多的TOP10图书")
    public Result<List<HotBookVO>> getHotBooks() {
        return statService.getHotBooks();
    }

    @GetMapping("/dashboard")
    @Operation(summary = "总体统计", description = "获取数据看板总体统计数据")
    public Result<DashboardVO> getDashboard() {
        return statService.getDashboard();
    }
}