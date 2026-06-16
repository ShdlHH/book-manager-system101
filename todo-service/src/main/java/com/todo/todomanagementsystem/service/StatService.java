package com.todo.todomanagementsystem.service;

import com.todo.todomanagementsystem.vo.BorrowTrendVO;
import com.todo.todomanagementsystem.vo.CategoryRatioVO;
import com.todo.todomanagementsystem.vo.HotBookVO;
import com.todo.todomanagementsystem.vo.DashboardVO;
import com.todo.todomanagementsystem.util.Result;

import java.util.List;

public interface StatService {

    Result<List<BorrowTrendVO>> getBorrowTrend(Integer days);

    Result<List<CategoryRatioVO>> getCategoryRatio();

    Result<List<HotBookVO>> getHotBooks();

    Result<DashboardVO> getDashboard();
}