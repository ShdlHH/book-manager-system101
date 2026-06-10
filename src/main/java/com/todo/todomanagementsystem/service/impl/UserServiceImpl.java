package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.config.JwtConfig;
import com.todo.todomanagementsystem.entity.User;
import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.service.UserService;
import com.todo.todomanagementsystem.mapper.UserMapper;
import com.todo.todomanagementsystem.util.JwtUtil;
import com.todo.todomanagementsystem.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author shdl
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2026-06-10 20:45:22
*/

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Result<?> getUserInfo() {
        Long userId = JwtConfig.currentUserId.get();
        User user = getById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @Override
    public Result<?> login(String username, String password) {
//        查询用户
        User user = lambdaQuery().eq(User::getUsername, username)
                .one();
        if (user == null){
            throw new BusinessException("用户名不存在");
        }
        if (!(user.getPassword().equals(password))){
            throw new BusinessException("密码错误");
        }

//         生成jwt令牌
        String token = jwtUtil.creatToken(user.getId());

//        封装返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
//         密码脱敏返回
        user.setPassword(null);
        map.put("user",user);
        return Result.success(map);
    }

    @Override
    public Result<?> register(String username, String password) {
//        用户是否存在
        boolean exists = lambdaQuery().eq(User::getUsername, username)
                .exists();

//        不存在就新增用户
        if(!exists){
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setCreateTime(new Date());
            save(user);
        }

        return Result.success("注册成功");
    }
}




