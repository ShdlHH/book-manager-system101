package com.todo.todomanagementsystem.config;



import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtConfig implements HandlerInterceptor {
    @Autowired
    private  JwtUtil jwtUtil;

    // 存放当前登录用户ID
    public static ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.contains("/user/login") || uri.contains("/user/register") ){
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || !jwtUtil.verifyToken(token)) {
            throw new BusinessException("请先登录");
        }
        Long userId = jwtUtil.getUserIdByToken(token);
        currentUserId.set(userId);
        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        currentUserId.remove();
    }
}