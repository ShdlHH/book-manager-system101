package com.todo.todomanagementsystem.dto;

import lombok.Data;

@Data
public class CategoryPageDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;
}
