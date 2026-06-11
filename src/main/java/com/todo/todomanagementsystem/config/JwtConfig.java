package com.todo.todomanagementsystem.config;



import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Slf4j
@Component
public class JwtConfig implements HandlerInterceptor {
    @Autowired
    private  JwtUtil jwtUtil;

    // 存放当前登录用户ID
    public static ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.info("请求URI: {}, 请求方法: {}", uri, method);
        
        // 放行OPTIONS预检请求
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        
        if (uri.contains("/user/login") || uri.contains("/user/register")){
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("原始token: {}", token);
        if (token == null || token.isEmpty() || !jwtUtil.verifyToken(token)) {
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