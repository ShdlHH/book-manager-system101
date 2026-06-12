package com.todo.todomanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "借阅趋势VO")
public class BorrowTrendVO {

    @Schema(description = "日期", example = "2026-06-11")
    private String date;

    @Schema(description = "借阅数量", example = "10")
    private Integer borrowCount;
}