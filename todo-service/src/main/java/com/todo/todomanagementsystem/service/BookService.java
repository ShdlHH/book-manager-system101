package com.todo.todomanagementsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.dto.BookAddDTO;
import com.todo.todomanagementsystem.dto.BookPageDTO;
import com.todo.todomanagementsystem.dto.BookStatusDTO;
import com.todo.todomanagementsystem.dto.BookUpdateDTO;
import com.todo.todomanagementsystem.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.BookDetailVO;

/**
* @author shdl
* @description 针对表【book】的数据库操作Service
* @createDate 2026-06-11 20:34:34
*/
public interface BookService extends IService<Book> {

    Result<Page<Book>> bookPage(BookPageDTO dto);

    Result<BookDetailVO> getBookDetail(Long id);

    Result<?> addBook(BookAddDTO dto);

Result<?> updateBook(Long id, BookUpdateDTO dto);

Result<?> deleteBook(Long id);

Result<?> updateBookStatus(Long id, BookStatusDTO dto);
}