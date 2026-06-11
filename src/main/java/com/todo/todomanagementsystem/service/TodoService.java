package com.todo.todomanagementsystem.service;

import com.todo.todomanagementsystem.entity.Todo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.todomanagementsystem.util.Result;

import java.util.List;

/**
* @author shdl
* @description 针对表【todo(待办事项表)】的数据库操作Service
* @createDate 2026-06-10 20:43:22
*/
public interface TodoService extends IService<Todo> {

    Result<?> getTodoList(Integer pageNum, Integer pageSize, String title, String status, String priority);

    Result<?> addTodo(Todo todo);

    Result<?> deleteTodo(Long id);

    Result<?> updateTodo(Todo todo);

    Result<?> changeStatus(Long id);

    Result<?> batchDeleteTodo(List<Long> ids);
}
