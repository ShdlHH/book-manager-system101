package com.todo.todomanagementsystem.service;

import com.todo.todomanagementsystem.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.todomanagementsystem.util.Result;

/**
* @author shdl
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2026-06-10 20:45:22
*/
public interface UserService extends IService<User> {

    Result<?> register(String username, String password);

    Result<?> login(String username, String password);

    Result<?> getUserInfo();
}
