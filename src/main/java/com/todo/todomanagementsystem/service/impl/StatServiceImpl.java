package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.todo.todomanagementsystem.dto.*;
import com.todo.todomanagementsystem.entity.Book;
import com.todo.todomanagementsystem.entity.BorrowRecord;
import com.todo.todomanagementsystem.entity.Category;
import com.todo.todomanagementsystem.entity.SysUser;
import com.todo.todomanagementsystem.service.*;
import com.todo.todomanagementsystem.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Override
    public Result<List<BorrowTrendVO>> getBorrowTrend(Integer days) {
        if (days == null || days <= 0) {
            days = 7;
        }

        List<BorrowTrendVO> trendList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -(days - 1));

        for (int i = 0; i < days; i++) {
            Date date = calendar.getTime();
            String dateStr = sdf.format(date);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date nextDate = calendar.getTime();

            long count = borrowRecordService.lambdaQuery()
                    .ge(BorrowRecord::getBorrowDate, date)
                    .lt(BorrowRecord::getBorrowDate, nextDate)
                    .count();

            trendList.add(new BorrowTrendVO(dateStr, (int) count));
        }

        return Result.success(trendList);
    }

    @Override
    public Result<List<CategoryRatioVO>> getCategoryRatio() {
        List<Category> categories = categoryService.list();
        long totalBooks = bookService.lambdaQuery().eq(Book::getStatus, 1).count();

        List<CategoryRatioVO> ratioList = categories.stream().map(category -> {
            long count = bookService.lambdaQuery()
                    .eq(Book::getCategoryId, category.getId())
                    .eq(Book::getStatus, 1)
                    .count();
            double ratio = totalBooks > 0 ? (count * 100.0 / totalBooks) : 0;
            return new CategoryRatioVO(
                    category.getId(),
                    category.getName(),
                    (int) count,
                    Math.round(ratio * 100.0) / 100.0
            );
        }).collect(Collectors.toList());

        return Result.success(ratioList);
    }

    @Override
    public Result<List<HotBookVO>> getHotBooks() {
        List<BorrowRecord> records = borrowRecordService.list();
        
        var bookCountMap = records.stream()
                .collect(Collectors.groupingBy(
                        BorrowRecord::getBookId,
                        Collectors.counting()
                ));

        List<HotBookVO> hotBooks = bookCountMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(entry -> {
                    Book book = bookService.getById(entry.getKey());
                    if (book == null) {
                        return null;
                    }
                    return new HotBookVO(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor(),
                            entry.getValue().intValue()
                    );
                })
                .filter(v -> v != null)
                .collect(Collectors.toList());

        return Result.success(hotBooks);
    }

    @Override
    public Result<DashboardVO> getDashboard() {
        long totalBooks = bookService.lambdaQuery().eq(Book::getStatus, 1).count();
        long totalUsers = userService.count();
        long borrowingCount = borrowRecordService.lambdaQuery()
                .in(BorrowRecord::getStatus, 0, 3)
                .count();
        
        Date now = new Date();
        long overdueCount = borrowRecordService.lambdaQuery()
                .in(BorrowRecord::getStatus, 0, 3)
                .lt(BorrowRecord::getDueDate, now)
                .count();

        DashboardVO dashboard = new DashboardVO((int) totalBooks, (int) totalUsers, (int) borrowingCount, (int) overdueCount);
        return Result.success(dashboard);
    }
}