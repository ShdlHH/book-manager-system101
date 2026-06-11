package com.todo.todomanagementsystem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.dto.UserPageDTO;
import com.todo.todomanagementsystem.service.UserService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.UserListVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService userService;

    //    注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody Map<String, String> params){
        return userService.register(params.get("username"), params.get("password"));
    }




    //    登录
    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params){
        return userService.login(params.get("username"), params.get("password"));
    }

//    获取用户信息
    @GetMapping("/info")
    public Result<?> getUserInfo(){
        return userService.getUserInfo();
    }
    //    获取用户信息
    @PutMapping("/info")
    public Result<?> updateUser(@RequestBody  Map<String, String> params){
        return userService.updateUser(params.get("nickname"),params.get("phone"),params.get("password"));
    }
    //    登出
    @PostMapping("/logout")
    public Result<?> logout(){
        return userService.logout();
    }

    /**
     * 分页查询用户列表
     * 权限：仅管理员
     * @param dto 分页+查询条件
     * @return 统一分页结果
     */
    @GetMapping("/page")
    public Result<Page<UserListVO>> userPage(UserPageDTO dto) {
        return userService.userPage(dto);
    }

}