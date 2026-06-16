package com.todo.todomanagementsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.dto.BookAddDTO;
import com.todo.todomanagementsystem.dto.BookPageDTO;
import com.todo.todomanagementsystem.dto.BookStatusDTO;
import com.todo.todomanagementsystem.dto.BookUpdateDTO;
import com.todo.todomanagementsystem.entity.Book;
import com.todo.todomanagementsystem.service.BookService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.BookDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
@Tag(name = "书籍管理", description = "书籍相关接口")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "分页查询书籍", description = "分页查询书籍列表，支持按书名、作者、分类筛选")
    @GetMapping("/page")
    public Result<Page<Book>> bookPage(BookPageDTO dto) {
        return bookService.bookPage(dto);
    }

    @Operation(summary = "获取书籍详情", description = "根据书籍ID获取书籍详细信息")
    @GetMapping("/{id}")
    public Result<BookDetailVO> getBookDetail(@Parameter(description = "书籍ID") @PathVariable Long id) {
        return bookService.getBookDetail(id);
    }

    @Operation(summary = "新增书籍", description = "新增书籍信息（仅管理员）")
    @PostMapping
    public Result<?> addBook(@RequestBody BookAddDTO dto) {
        return bookService.addBook(dto);
    }

    @Operation(summary = "修改书籍", description = "修改书籍信息（仅管理员）")
    @PutMapping("/{id}")
    public Result<?> updateBook(
            @Parameter(description = "书籍ID") @PathVariable Long id,
            @RequestBody BookUpdateDTO dto) {
        return bookService.updateBook(id, dto);
    }

    @Operation(summary = "删除书籍", description = "删除书籍（仅管理员）")
    @DeleteMapping("/{id}")
    public Result<?> deleteBook(@Parameter(description = "书籍ID") @PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @Operation(summary = "更新书籍状态", description = "上架/下架书籍（仅管理员）")
    @PutMapping("/{id}/status")
    public Result<?> updateBookStatus(
            @Parameter(description = "书籍ID") @PathVariable Long id,
            @RequestBody BookStatusDTO dto) {
        return bookService.updateBookStatus(id, dto);
    }
}