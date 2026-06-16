package com.todo.todomanagementsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.dto.UserAddDTO;
import com.todo.todomanagementsystem.dto.UserPageDTO;
import com.todo.todomanagementsystem.dto.UserStatusDTO;
import com.todo.todomanagementsystem.dto.UserUpdateDTO;
import com.todo.todomanagementsystem.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.UserListVO;

/**
* @author shdl
* @description 用户管理Service
* @createDate 2026-06-10 20:45:22
*/
public interface UserService extends IService<SysUser> {

    Result<?> register(String username, String password);

    Result<?> login(String username, String password);

    Result<?> getUserInfo();

    Result<?> updateUser(String nickname, String phone, String password);

    Result<?> logout();

    Result<Page<UserListVO>> userPage(UserPageDTO dto);

    Result<?> addUser(UserAddDTO dto);

    Result<?> updateUserByAdmin(Long id, UserUpdateDTO dto);

    Result<?> updateUserStatus(Long id, UserStatusDTO dto);

    Result<?> deleteUser(Long id);
}