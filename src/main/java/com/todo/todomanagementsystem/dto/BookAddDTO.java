package com.todo.todomanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "新增图书入参")
public class BookAddDTO {
    @Schema(description = "ISBN编号", example = "978-7-111-54493-7")
    private String isbn;

    @Schema(description = "书名", example = "Java编程思想")
    private String title;

    @Schema(description = "作者", example = "Bruce Eckel")
    private String author;

    @Schema(description = "出版社", example = "机械工业出版社")
    private String publisher;

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "封面图URL", example = "https://example.com/cover.jpg")
    private String cover;

    @Schema(description = "简介", example = "Java学习经典书籍")
    private String description;

    @Schema(description = "总库存", example = "100")
    private Integer totalNum;

    @Schema(description = "价格", example = "89.00")
    private BigDecimal price;
}