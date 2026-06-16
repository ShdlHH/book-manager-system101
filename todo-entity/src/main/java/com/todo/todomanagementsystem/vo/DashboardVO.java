package com.todo.todomanagementsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据看板总体统计VO")
public class DashboardVO {

    @Schema(description = "书籍总数", example = "1000")
    private Integer totalBooks;

    @Schema(description = "读者总人数", example = "500")
    private Integer totalUsers;

    @Schema(description = "当前借阅中数量", example = "100")
    private Integer borrowingCount;

    @Schema(description = "逾期数量", example = "10")
    private Integer overdueCount;
}