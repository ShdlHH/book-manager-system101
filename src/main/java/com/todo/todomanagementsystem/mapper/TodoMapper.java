package com.todo.todomanagementsystem.mapper;

import com.todo.todomanagementsystem.entity.Todo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author shdl
* @description 针对表【todo(待办事项表)】的数据库操作Mapper
* @createDate 2026-06-10 20:43:22
* @Entity com.todo.todomanagementsystem.entity.Todo
*/
@Mapper
public interface TodoMapper extends BaseMapper<Todo> {

}




