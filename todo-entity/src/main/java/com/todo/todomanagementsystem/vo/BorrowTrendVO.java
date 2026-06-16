package com.todo.todomanagementsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "鍊熼槄瓒嬪娍VO")
public class BorrowTrendVO {

    @Schema(description = "鏃ユ湡", example = "2026-06-11")
    private String date;

    @Schema(description = "鍊熼槄鏁伴噺", example = "10")
    private Integer borrowCount;
}
