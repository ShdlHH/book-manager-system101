package com.todo.todomanagementsystem.dto;


import lombok.Data;

/**
 * 管理员新增用户入参
 */
@Data
public class UserAddDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 角色：admin/reader
     */
    private int role;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 最大可借数量
     */
    private Integer maxBorrowNum;
}