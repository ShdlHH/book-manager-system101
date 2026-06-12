package com.todo.todomanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类占比VO")
public class CategoryRatioVO {

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "分类名称", example = "文学小说")
    private String categoryName;

    @Schema(description = "图书数量", example = "50")
    private Integer bookCount;

    @Schema(description = "占比", example = "25.00")
    private Double ratio;
}