package com.todo.todomanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "借阅记录分页查询参数")
public class BorrowPageDTO {
    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "借阅人（模糊搜索）", example = "reader01")
    private String username;

    @Schema(description = "书名（模糊查询）", example = "Java")
    private String bookTitle;

    @Schema(description = "状态：0-借阅中，1-已归还，2-已逾期", example = "0")
    private Integer status;
}
