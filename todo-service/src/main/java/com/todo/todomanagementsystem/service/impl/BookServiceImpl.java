package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.util.ThreadLocalContext;
import com.todo.todomanagementsystem.dto.BookAddDTO;
import com.todo.todomanagementsystem.dto.BookPageDTO;
import com.todo.todomanagementsystem.dto.BookStatusDTO;
import com.todo.todomanagementsystem.dto.BookUpdateDTO;
import com.todo.todomanagementsystem.entity.Book;
import com.todo.todomanagementsystem.entity.Category;
import com.todo.todomanagementsystem.entity.SysUser;
import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.service.BookService;
import com.todo.todomanagementsystem.mapper.BookMapper;
import com.todo.todomanagementsystem.service.CategoryService;
import com.todo.todomanagementsystem.service.SysUserService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.BookDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result<?> addBook(BookAddDTO dto) {
        String username = ThreadLocalContext.getCurrentUserName();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException(403, "无权限操作");
        }

        boolean exists = lambdaQuery().eq(Book::getIsbn, dto.getIsbn()).exists();
        if (exists) {
            throw new BusinessException("ISBN已存在");
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryService.getById(dto.getCategoryId());
            if (category == null) {
                throw new BusinessException("分类不存在");
            }
        }

        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        book.setAvailableNum(dto.getTotalNum());
        book.setStatus(1);
        book.setCreateTime(new Date());
        book.setUpdateTime(new Date());

        save(book);
        return Result.success("新增图书成功");
    }

    @Override
    public Result<BookDetailVO> getBookDetail(Long id) {
        Book book = getById(id);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }

        String categoryName = null;
        if (book.getCategoryId() != null) {
            Category category = categoryService.getById(book.getCategoryId());
            if (category != null) {
                categoryName = category.getName();
            }
        }

        BookDetailVO vo = new BookDetailVO();
        BeanUtils.copyProperties(book, vo);
        vo.setCategoryName(categoryName);
        if (book.getCreateTime() != null) {
            vo.setCreateTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(book.getCreateTime()));
        }

        return Result.success(vo);
    }

    @Override
    public Result<Page<Book>> bookPage(BookPageDTO dto) {
        Page<Book> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<Book> bookPage = lambdaQuery()
                .like(dto.getTitle() != null && !dto.getTitle().isEmpty(), Book::getTitle, dto.getTitle())
                .like(dto.getAuthor() != null && !dto.getAuthor().isEmpty(), Book::getAuthor, dto.getAuthor())
                .eq(dto.getCategoryId() != null, Book::getCategoryId, dto.getCategoryId())
                .eq(dto.getStatus() != null, Book::getStatus, dto.getStatus())
                .orderByDesc(Book::getCreateTime)
                .page(page);
        return Result.success(bookPage);
    }

    @Override
    public Result<?> updateBook(Long id, BookUpdateDTO dto) {
        String username = ThreadLocalContext.getCurrentUserName();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException(403, "无权限操作");
        }

        Book book = getById(id);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }

        if (dto.getIsbn() != null && !dto.getIsbn().equals(book.getIsbn())) {
            boolean exists = lambdaQuery().eq(Book::getIsbn, dto.getIsbn()).ne(Book::getId, id).exists();
            if (exists) {
                throw new BusinessException("ISBN已存在");
            }
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryService.getById(dto.getCategoryId());
            if (category == null) {
                throw new BusinessException("分类不存在");
            }
        }

        BeanUtils.copyProperties(dto, book);
        if (dto.getTotalNum() != null) {
            book.setAvailableNum(book.getAvailableNum() + (dto.getTotalNum() - book.getTotalNum()));
        }
        book.setUpdateTime(new Date());

        updateById(book);
        return Result.success("修改图书成功");
    }

    @Override
    public Result<?> deleteBook(Long id) {
        String username = ThreadLocalContext.getCurrentUserName();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException(403, "无权限操作");
        }

        Book book = getById(id);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }

        removeById(id);
        return Result.success("删除图书成功");
    }

    @Override
    public Result<?> updateBookStatus(Long id, BookStatusDTO dto) {
        String username = ThreadLocalContext.getCurrentUserName();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException(403, "无权限操作");
        }

        Book book = getById(id);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }

        book.setStatus(dto.getStatus());
        book.setUpdateTime(new Date());
        updateById(book);
        return Result.success("更新图书状态成功");
    }
}
