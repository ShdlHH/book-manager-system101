package com.todo.todomanagementsystem.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserListVO {
    private Long id;
    private String username;
    private String nickname;
    // 鍓嶇灞曠ず瀛楃涓诧細admin / reader
    private Integer role;
    private String phone;
    private Integer maxBorrowNum;
    private Integer status;
    private String createTime;
}
