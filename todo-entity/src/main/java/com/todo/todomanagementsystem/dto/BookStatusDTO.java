package com.todo.todomanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新图书状态入参")
public class BookStatusDTO {
    @Schema(description = "状态：0-下架，1-上架", example = "0")
    private Integer status;
}