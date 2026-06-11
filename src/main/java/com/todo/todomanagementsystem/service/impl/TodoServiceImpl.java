package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.config.JwtConfig;
import com.todo.todomanagementsystem.entity.Todo;
import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.service.TodoService;
import com.todo.todomanagementsystem.mapper.TodoMapper;
import com.todo.todomanagementsystem.util.Result;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author shdl
* @description 针对表【todo(待办事项表)】的数据库操作Service实现
* @createDate 2026-06-10 20:43:22
*/
@Slf4j
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo>
    implements TodoService{

    private Long getCurrentUserId(){
        log.info("userId,{}",JwtConfig.currentUserId.get());
        return JwtConfig.currentUserId.get();
    }

    @Override
    public Result<?> addTodo(Todo todo) {
        todo.setUserId(getCurrentUserId());
        todo.setCreateTime(new Date());
        todo.setUpdateTime(new Date());
        save(todo);
        return Result.success("保存代办完成");
    }

    @Override
    public Result<?> deleteTodo(Long id) {
        Todo todo = getById(id);
        if(todo == null || !todo.getUserId().equals(getCurrentUserId())){
            throw new BusinessException("无权操作数据");
        }
        removeById(id);
        return Result.success("删除代办成功");

    }

    @Override
    public Result<?> updateTodo(Todo todo) {
        Todo oldtodo = getById(todo.getId());
        if(oldtodo == null || !oldtodo.getUserId().equals(getCurrentUserId())){
            throw new BusinessException("无权操作数据");
        }
        BeanUtils.copyProperties(todo,oldtodo);
        oldtodo.setUpdateTime(new Date());
        updateById(oldtodo);
        return Result.success("修改代办成功");
    }

    @Override
    public Result<?> changeStatus(Long id) {
        Todo todo = getById(id);
        if(todo == null || !todo.getUserId().equals(getCurrentUserId())){
            throw new BusinessException("无权操作数据");
        }
        todo.setStatus(todo.getStatus()==0?1:0);
        todo.setUpdateTime(new Date());
        updateById(todo);
        return Result.success("修改状态成功");
    }

    @Override
    public Result<?> batchDeleteTodo(List<Long> ids) {
        Long userId = getCurrentUserId();
        for (Long id:ids){
            Todo todo = getById(id);
            if(todo == null || !todo.getUserId().equals(getCurrentUserId())){
                throw new BusinessException("无权操作数据");
            }
        }
        removeBatchByIds(ids);
        return Result.success("批量删除代办成功");
    }

    @Override
    public Result<?> getTodoList(Integer pageNum, Integer pageSize, String title, String status, String priority) {
        Long userId = getCurrentUserId();
        Page<Todo> page = new Page<>(pageNum,pageSize);
        LambdaQueryChainWrapper<Todo> wrapper = lambdaQuery();
        wrapper.eq(Todo::getUserId,userId);
        if(!StringUtils.isBlank(title)){
            wrapper.like(Todo::getTitle,title);
        }
        if(!StringUtils.isBlank(status)){
            wrapper.eq(Todo::getStatus,status);
        }
        if(!StringUtils.isBlank(priority)){
            wrapper.eq(Todo::getPriority,priority);
        }
        IPage<Todo> result = page(page,wrapper.getWrapper());

        return Result.success(result);

    }
}




