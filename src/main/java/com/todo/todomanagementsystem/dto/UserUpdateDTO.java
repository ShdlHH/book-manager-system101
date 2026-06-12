package com.todo.todomanagementsystem.dto;

import lombok.Data;

/**
 * 管理员修改用户入参
 */
@Data
public class UserUpdateDTO {
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 角色：admin/reader
     */
    private Integer role;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 最大可借数量
     */
    private Integer maxBorrowNum;
}
