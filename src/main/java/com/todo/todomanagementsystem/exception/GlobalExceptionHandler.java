package com.todo.todomanagementsystem.exception;


import com.todo.todomanagementsystem.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
//捕获自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e){
        return  Result.error(e.getMessage());
    }



// 捕获全局未知异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        e.printStackTrace();
        return Result.error("服务器内部异常");

    }

}