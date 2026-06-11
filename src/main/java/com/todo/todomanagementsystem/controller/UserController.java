package com.todo.todomanagementsystem.controller;


import com.todo.todomanagementsystem.service.UserService;
import com.todo.todomanagementsystem.util.Result;
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

}