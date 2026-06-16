package com.todo.todomanagementsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.dto.UserAddDTO;
import com.todo.todomanagementsystem.dto.UserPageDTO;
import com.todo.todomanagementsystem.dto.UserStatusDTO;
import com.todo.todomanagementsystem.dto.UserUpdateDTO;
import com.todo.todomanagementsystem.service.UserService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.UserListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "注册", description = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@RequestBody Map<String, String> params) {
        return userService.register(params.get("username"), params.get("password"));
    }

    @Operation(summary = "登录", description = "用户登录")
    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        return userService.login(params.get("username"), params.get("password"));
    }

    @Operation(summary = "获取用户信息", description = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<?> getUserInfo() {
        return userService.getUserInfo();
    }

    @Operation(summary = "更新用户信息", description = "更新当前登录用户信息")
    @PutMapping("/info")
    public Result<?> updateUser(@RequestBody Map<String, String> params) {
        return userService.updateUser(params.get("nickname"), params.get("phone"), params.get("password"));
    }

    @Operation(summary = "登出", description = "用户登出")
    @PostMapping("/logout")
    public Result<?> logout() {
        return userService.logout();
    }

    @Operation(summary = "分页查询用户", description = "分页查询用户列表（仅管理员）")
    @GetMapping("/page")
    public Result<Page<UserListVO>> userPage(UserPageDTO dto) {
        return userService.userPage(dto);
    }

    @Operation(summary = "新增用户", description = "管理员新增用户")
    @PostMapping
    public Result<?> addUser(@RequestBody UserAddDTO dto) {
        return userService.addUser(dto);
    }

    @Operation(summary = "修改用户", description = "管理员修改用户信息")
    @PutMapping("/{id}")
    public Result<?> updateUserByAdmin(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody UserUpdateDTO dto) {
        return userService.updateUserByAdmin(id, dto);
    }

    @Operation(summary = "更新用户状态", description = "管理员更新用户状态")
    @PutMapping("/{id}/status")
    public Result<?> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody UserStatusDTO dto) {
        return userService.updateUserStatus(id, dto);
    }

    @Operation(summary = "删除用户", description = "管理员删除用户")
    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        return userService.deleteUser(id);
    }
}