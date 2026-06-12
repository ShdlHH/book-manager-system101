package com.todo.todomanagementsystem.controller;

import com.todo.todomanagementsystem.entity.Todo;
import com.todo.todomanagementsystem.service.TodoService;
import com.todo.todomanagementsystem.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@Tag(name = "待办管理", description = "待办事项相关接口")
public class TodoController {

    @Resource
    private TodoService todoService;

    @Operation(summary = "分页查询待办", description = "分页查询待办列表，支持按标题、状态、优先级筛选")
    @GetMapping("/list")
    public Result<?> getList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "标题（模糊查询）") @RequestParam(required = false) String title,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "优先级") @RequestParam(required = false) String priority) {
        return todoService.getTodoList(pageNum, pageSize, title, status, priority);
    }

    @Operation(summary = "新增待办", description = "新增待办事项")
    @PostMapping("/add")
    public Result<?> add(@RequestBody Todo todo) {
        return todoService.addTodo(todo);
    }

    @Operation(summary = "修改待办", description = "修改待办事项")
    @PutMapping("/update")
    public Result<?> update(@RequestBody Todo todo) {
        return todoService.updateTodo(todo);
    }

    @Operation(summary = "删除待办", description = "单条删除待办事项")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@Parameter(description = "待办ID") @PathVariable Long id) {
        return todoService.deleteTodo(id);
    }

    @Operation(summary = "批量删除待办", description = "批量删除待办事项")
    @DeleteMapping("/deleteBatch")
    public Result<?> batchDelete(@RequestBody List<Long> ids) {
        return todoService.batchDeleteTodo(ids);
    }

    @Operation(summary = "切换状态", description = "切换待办事项状态")
    @PutMapping("/changeStatus/{id}")
    public Result<?> changeStatus(@Parameter(description = "待办ID") @PathVariable Long id) {
        return todoService.changeStatus(id);
    }
}