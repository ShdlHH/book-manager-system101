package com.todo.todomanagementsystem.dto;

import lombok.Data;

@Data
public class UserPageDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String username;
    private String nickname;
    private Integer role;
}
