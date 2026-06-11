package com.todo.todomanagementsystem.dto;

import lombok.Data;

/**
 * 用户分页查询入参
 */
@Data
public class UserPageDTO {
    /**
     * 页码，默认1
     */
    private Integer page =1;

    /**
     * 每页条数，默认10
     */
    private Integer size = 10;

    /**
     * 用户名（模糊查询）
     */
    private String username;

    /**
     * 昵称（模糊查询）
     */
    private String nickname;

    /**
     * 角色：admin/reader
     */
    private Integer role;
}