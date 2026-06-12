package com.todo.todomanagementsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.dto.BorrowAddDTO;
import com.todo.todomanagementsystem.dto.BorrowPageDTO;
import com.todo.todomanagementsystem.dto.BorrowVO;
import com.todo.todomanagementsystem.entity.BorrowRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.todomanagementsystem.util.Result;

public interface BorrowRecordService extends IService<BorrowRecord> {

    Result<?> borrowBook(BorrowAddDTO dto);

    Result<?> returnBook(Long id);

    Result<?> renewBook(Long id);

    Result<Page<BorrowVO>> getMyBorrowList();

    Result<Page<BorrowVO>> getBorrowPage(BorrowPageDTO dto);

    Result<Page<BorrowVO>> getOverdueList(Integer page, Integer size);
}