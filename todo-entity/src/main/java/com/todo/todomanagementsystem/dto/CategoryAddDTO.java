// src/main/java/com/todo/todomanagementsystem/dto/CategoryAddDTO.java
package com.todo.todomanagementsystem.dto;

import lombok.Data;

/**
 * 新增分类入参
 */
@Data
public class CategoryAddDTO {
    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序号
     */
    private Integer sort;
}