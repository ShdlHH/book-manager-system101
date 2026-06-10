package com.todo.todomanagementsystem.exception;

public class BusinessException extends RuntimeException {
//直接构造
    public BusinessException(String message){
        super(message);
    }
}