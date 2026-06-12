// src/main/java/com/todo/todomanagementsystem/vo/BookDetailVO.java
package com.todo.todomanagementsystem.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 图书详情VO
 */
@Data
public class BookDetailVO {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Long categoryId;
    private String categoryName;
    private String cover;
    private String description;
    private Integer totalNum;
    private Integer availableNum;
    private BigDecimal price;
    private Integer status;
    private String createTime;
}
