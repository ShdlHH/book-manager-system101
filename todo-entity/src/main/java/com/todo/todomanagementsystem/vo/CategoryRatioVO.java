package com.todo.todomanagementsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "鍒嗙被鍗犳瘮VO")
public class CategoryRatioVO {

    @Schema(description = "鍒嗙被ID", example = "1")
    private Long categoryId;

    @Schema(description = "鍒嗙被鍚嶇О", example = "鏂囧灏忚")
    private String categoryName;

    @Schema(description = "鍥句功鏁伴噺", example = "50")
    private Integer bookCount;

    @Schema(description = "鍗犳瘮", example = "25.00")
    private Double ratio;
}
