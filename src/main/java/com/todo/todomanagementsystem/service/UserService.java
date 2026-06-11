package com.todo.todomanagementsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.dto.UserPageDTO;
import com.todo.todomanagementsystem.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.UserListVO;

/**
* @author shdl
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2026-06-10 20:45:22
*/
public interface UserService extends IService<User> {

    Result<?> register(String username, String password);

    Result<?> login(String username, String password);

    Result<?> getUserInfo();



    Result<?> updateUser(String nickname, String phone, String password);

    Result<?> logout();

    Result<Page<UserListVO>> userPage(UserPageDTO dto);
}
