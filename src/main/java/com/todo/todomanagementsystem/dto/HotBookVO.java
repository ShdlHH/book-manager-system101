package com.todo.todomanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热门图书VO")
public class HotBookVO {

    @Schema(description = "图书ID", example = "1")
    private Long bookId;

    @Schema(description = "书名", example = "深入理解计算机系统")
    private String title;

    @Schema(description = "作者", example = "兰德尔·E·布莱恩特")
    private String author;

    @Schema(description = "借阅次数", example = "150")
    private Integer borrowCount;
}