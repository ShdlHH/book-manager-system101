package com.todo.todomanagementsystem.mapper;

import com.todo.todomanagementsystem.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author shdl
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2026-06-10 20:45:22
* @Entity com.todo.todomanagementsystem.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




