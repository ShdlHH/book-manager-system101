package com.todo.todomanagementsystem.vo;

import lombok.Data;

/**
 * 用户视图对象（登录返回VO）
 */
@Data
public class UserInfoVO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名/账号
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 角色标识
     */
    private Integer role;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 最大可借数量
     */
    private Integer maxBorrowNum;

    private Integer status;
}