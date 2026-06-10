package com.todo.todomanagementsystem;

import com.todo.todomanagementsystem.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoManagementSystemApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(TodoManagementSystemApplicationTests.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void contextLoads() {
    }

    @Test
    void testJwtTokenGenerationAndVerification() {
        // 1. 测试生成 token
        String token = jwtUtil.creatToken(1L);
        logger.info("生成的 token: {}", token);
        assertNotNull(token, "token 不应为 null");
        assertFalse(token.isEmpty(), "token 不应为空");

        // 2. 测试解析 token
        Long userIdByToken = jwtUtil.getUserIdByToken(token);
        logger.info("解析出的 userId: {}", userIdByToken);
        assertNotNull(userIdByToken, "解析出的 userId 不应为 null");
        assertEquals(1L, userIdByToken, "解析出的 userId 应等于 1");

        // 3. 测试验证 token
        boolean isValid = jwtUtil.verifyToken(token);
        logger.info("token 验证结果: {}", isValid);
        assertTrue(isValid, "token 应验证通过");
    }

}