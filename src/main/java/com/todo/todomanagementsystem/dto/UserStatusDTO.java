package com.todo.todomanagementsystem.dto;


import lombok.Data;

/**
 * 更新用户状态入参
 */
@Data
public class UserStatusDTO {
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}