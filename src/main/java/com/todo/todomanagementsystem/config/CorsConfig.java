package com.todo.todomanagementsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置跨域资源共享(CORS)策略
        registry.addMapping("/**")                         // 对所有路径启用CORS
                .allowedOriginPatterns("*")                       // 允许所有来源访问（支持 allowCredentials）
                .allowedMethods("GET","PUT","POST","DELETE") // 允许的HTTP请求方法
                .allowCredentials(true)                    // 是否允许发送Cookie
                .maxAge(3600);                            // 预检请求缓存时间(秒)
    }
}