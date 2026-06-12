// src/main/java/com/todo/todomanagementsystem/dto/CategoryPageDTO.java
package com.todo.todomanagementsystem.dto;

import lombok.Data;

/**
 * 分类分页查询入参
 */
@Data
public class CategoryPageDTO {
    /**
     * 页码，默认1
     */
    private Integer page = 1;

    /**
     * 每页条数，默认10
     */
    private Integer size = 10;

    /**
     * 分类名称（模糊查询）
     */
    private String name;
}
