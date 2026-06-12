// src/main/java/com/todo/todomanagementsystem/vo/CategoryVO.java
package com.todo.todomanagementsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类列表VO（用于下拉选择）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {
    private Long id;
    private String name;
}
