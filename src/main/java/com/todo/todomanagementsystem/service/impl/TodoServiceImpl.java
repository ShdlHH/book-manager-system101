package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.entity.Todo;
import com.todo.todomanagementsystem.service.TodoService;
import com.todo.todomanagementsystem.mapper.TodoMapper;
import org.springframework.stereotype.Service;

/**
* @author shdl
* @description 针对表【todo(待办事项表)】的数据库操作Service实现
* @createDate 2026-06-10 20:43:22
*/
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo>
    implements TodoService{

}




