package com.todo.todomanagementsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热门书籍VO")
public class HotBookVO {

    @Schema(description = "书籍ID", example = "1")
    private Long bookId;

    @Schema(description = "书名", example = "Java编程思想")
    private String title;

    @Schema(description = "作者", example = "Bruce Eckel")
    private String author;

    @Schema(description = "借阅次数", example = "150")
    private Integer borrowCount;
}