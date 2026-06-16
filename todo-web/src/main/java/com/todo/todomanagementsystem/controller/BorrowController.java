package com.todo.todomanagementsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.dto.BorrowAddDTO;
import com.todo.todomanagementsystem.dto.BorrowPageDTO;
import com.todo.todomanagementsystem.vo.BorrowVO;
import com.todo.todomanagementsystem.service.BorrowRecordService;
import com.todo.todomanagementsystem.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@Tag(name = "借阅管理", description = "借阅相关接口")
public class BorrowController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @Operation(summary = "借书", description = "用户借阅图书")
    @PostMapping
    public Result<?> borrowBook(@RequestBody BorrowAddDTO dto) {
        return borrowRecordService.borrowBook(dto);
    }

    @Operation(summary = "还书", description = "归还图书")
    @PutMapping("/{id}/return")
    public Result<?> returnBook(@Parameter(description = "借阅记录ID") @PathVariable Long id) {
        return borrowRecordService.returnBook(id);
    }

    @Operation(summary = "续借", description = "续借图书，延长应还日期14天")
    @PutMapping("/{id}/renew")
    public Result<?> renewBook(@Parameter(description = "借阅记录ID") @PathVariable Long id) {
        return borrowRecordService.renewBook(id);
    }

    @Operation(summary = "我的借阅记录", description = "获取当前用户的借阅记录")
    @GetMapping("/my-list")
    public Result<Page<BorrowVO>> getMyBorrowList() {
        return borrowRecordService.getMyBorrowList();
    }

    @Operation(summary = "全部借阅记录", description = "分页获取所有借阅记录（仅管理员）")
    @GetMapping("/page")
    public Result<Page<BorrowVO>> getBorrowPage(BorrowPageDTO dto) {
        return borrowRecordService.getBorrowPage(dto);
    }

    @Operation(summary = "逾期列表", description = "获取所有逾期未还的借阅记录（仅管理员）")
    @GetMapping("/overdue")
    public Result<Page<BorrowVO>> getOverdueList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        return borrowRecordService.getOverdueList(pageNum, pageSize);
    }
}

