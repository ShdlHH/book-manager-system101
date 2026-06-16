package com.todo.todomanagementsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "鏂囦欢涓婁紶鍝嶅簲")
public class FileUploadVO {
    @Schema(description = "鏂囦欢璁块棶URL", example = "https://xxx.com/uploads/20240101/book.jpg")
    private String url;
}
