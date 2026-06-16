package com.todo.todomanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "借书请求参数")
public class BorrowAddDTO {
    @Schema(description = "图书ID", example = "1")
    private Long bookId;
}