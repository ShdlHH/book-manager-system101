package com.todo.todomanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "图书分页查询入参")
public class BookPageDTO {
    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer size = 10;

    @Schema(description = "书名（模糊查询）", example = "Java")
    private String title;

    @Schema(description = "作者（模糊查询）", example = "Bruce")
    private String author;

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "状态：0-下架，1-上架", example = "1")
    private Integer status;
}