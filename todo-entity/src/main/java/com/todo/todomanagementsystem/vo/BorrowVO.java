package com.todo.todomanagementsystem.vo;

import com.todo.todomanagementsystem.entity.BorrowRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@Schema(description = "借阅记录VO")
public class BorrowVO {
    @Schema(description = "记录ID", example = "1")
    private Long id;

    @Schema(description = "用户ID", example = "2")
    private Long userId;

    @Schema(description = "用户名", example = "reader01")
    private String username;

    @Schema(description = "联系电话", example = "13800138000")
    private String phone;

    @Schema(description = "书籍ID", example = "1")
    private Long bookId;

    @Schema(description = "作者", example = "张三")
    private String author;

    @Schema(description = "书名", example = "Java编程思想")
    private String bookTitle;

    @Schema(description = "借阅日期")
    private Date borrowDate;

    @Schema(description = "应还日期")
    private Date dueDate;

    @Schema(description = "实际归还日期")
    private Date returnDate;

    @Schema(description = "状态：0-借阅中，1-已归还，2-已逾期", example = "0")
    private Integer status;

    @Schema(description = "续借次数", example = "0")
    private Integer renewCount;

    @Schema(description = "逾期天数")
    private Integer overdueDays;

    public static BorrowVO from(BorrowRecord record) {
        BorrowVO vo = new BorrowVO();
        BeanUtils.copyProperties(record, vo);
        return vo;
    }
}