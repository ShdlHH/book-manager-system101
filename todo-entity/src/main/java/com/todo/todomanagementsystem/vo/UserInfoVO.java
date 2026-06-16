package com.todo.todomanagementsystem.vo;

import lombok.Data;

/**
 * 鐢ㄦ埛瑙嗗浘瀵硅薄锛堢櫥褰曡繑鍥濾O锛?
 */
@Data
public class UserInfoVO {
    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 鐢ㄦ埛鍚?璐﹀彿
     */
    private String username;

    /**
     * 鏄电О
     */
    private String nickname;

    /**
     * 瑙掕壊鏍囪瘑
     */
    private Integer role;

    /**
     * 鎵嬫満鍙?
     */
    private String phone;

    /**
     * 澶村儚鍦板潃
     */
    private String avatar;

    /**
     * 鏈€澶у彲鍊熸暟閲?
     */
    private Integer maxBorrowNum;

    private Integer status;
}
