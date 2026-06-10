package com.todo.todomanagementsystem.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    //响应码
    private Integer code;
    //提示信息
    private String msg;
    //响应数据
    private T data ;

    //成功（有数据）
    public static  <T> Result <T> success(T data){
        return new Result<>(200,"操作成功",data);
    }

    //成功（无数据）
    public static  <T> Result <T> success(){
        return new Result<>(200,"操作成功",null);
    }
    //失败
    public static  <T> Result <T> error(String msg){
        return new Result<>(500,msg,null);
    }
}