// src/main/java/com/todo/todomanagementsystem/dto/CategoryUpdateDTO.java
package com.todo.todomanagementsystem.dto;

import lombok.Data;

/**
 * 修改分类入参
 */
@Data
public class CategoryUpdateDTO {
    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序号
     */
    private Integer sort;
}