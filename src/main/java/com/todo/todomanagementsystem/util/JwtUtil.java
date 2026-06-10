package com.todo.todomanagementsystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    //密钥
    private static final String SECRET = "todoSystemJwtSecretKey1234567890abcdef";
     //过期时间
    private static final long EXPIRE_TIME= 60 * 60 *1000;
    //生成的密钥
    private static final SecretKey KEY =Keys.hmacShaKeyFor(SECRET.getBytes()) ;

     //生成token
    public String creatToken(Long userId){
        // 获取当前时间
        Date now = new Date();
        // 计算token过期时间 = 当前时间 + 过期时长
        Date expireDate  = new Date(now.getTime()+EXPIRE_TIME);

        // 使用Jwts构建器生成token
        return Jwts.builder()
                .setSubject(userId.toString())  // 设置主题为用户ID
                .setExpiration(expireDate)      // 设置过期时间
                .setIssuedAt(now)               // 设置签发时间
                .signWith(KEY)                  // 使用密钥签名
                .compact();                     // 压缩生成最终token字符串

    }
     //解析token，从token中提取用户ID
    public Long getUserIdByToken(String token){
        // 创建JWT解析器构建器，配置签名密钥后构建解析器
        // parseClaimsJws方法解析token并验证签名，getBody获取载荷（claims）部分
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)           // 设置用于验证签名的密钥
                .build()                      // 构建JWT解析器
                .parseClaimsJws(token)        // 解析token字符串，验证签名有效性
                .getBody();                   // 获取token的载荷（claims）部分
        
        // 从claims中获取主题（subject），即用户ID，并转换为Long类型
        return Long.parseLong(claims.getSubject());

    }
     //是否有效
    public boolean verifyToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }




}