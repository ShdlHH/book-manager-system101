package com.todo.todomanagementsystem.exception;

import com.todo.todomanagementsystem.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        Integer code = e.getCode();
        if (code == 401) {
            return Result.unauthorized(e.getMessage());
        } else if (code == 403) {
            return Result.forbidden(e.getMessage());
        }
        return Result.badRequest(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("服务器内部错误");
    }
}
