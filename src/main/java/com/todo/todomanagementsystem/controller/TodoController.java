package com.todo.todomanagementsystem.controller;


import com.todo.todomanagementsystem.entity.Todo;
import com.todo.todomanagementsystem.service.TodoService;
import com.todo.todomanagementsystem.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {



    @Resource
    private TodoService todoService;

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param title
     * @param status
     * @param priority
     * @return
     */
    @GetMapping("/list")
    public Result<?> getList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title ,
            @RequestParam(required = false) String status ,
            @RequestParam(required = false) String priority
            ){

        return todoService.getTodoList(pageNum,pageSize,title,status,priority);

    }
    /**
     * 新增待办
     * POST /api/todo/add
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody Todo todo) {
        return todoService.addTodo(todo);
    }


    /**
     * 修改待办
     * PUT /api/todo/update
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody Todo todo) {
        return todoService.updateTodo(todo);
    }

    /**
     * 单条删除
     * DELETE /api/todo/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return todoService.deleteTodo(id);
    }

    /**
     * 批量删除
     * DELETE /api/todo/deleteBatch
     */
    @DeleteMapping("/deleteBatch")
    public Result<?> batchDelete(@RequestBody List<Long> ids) {
        return todoService.batchDeleteTodo(ids);
    }

    /**
     * 切换状态
     * PUT /api/todo/changeStatus/{id}
     */
    @PutMapping("/changeStatus/{id}")
    public Result<?> changeStatus(@PathVariable Long id) {
        return todoService.changeStatus(id);
    }

}
