package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.entity.Book;
import com.todo.todomanagementsystem.service.BookService;
import com.todo.todomanagementsystem.mapper.BookMapper;
import org.springframework.stereotype.Service;

/**
* @author shdl
* @description 针对表【book】的数据库操作Service实现
* @createDate 2026-06-11 20:34:34
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

}




