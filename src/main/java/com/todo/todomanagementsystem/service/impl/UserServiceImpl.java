package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.config.JwtConfig;
import com.todo.todomanagementsystem.dto.UserPageDTO;
import com.todo.todomanagementsystem.entity.SysUser;
import com.todo.todomanagementsystem.entity.User;
import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.service.SysUserService;
import com.todo.todomanagementsystem.service.UserService;
import com.todo.todomanagementsystem.mapper.UserMapper;
import com.todo.todomanagementsystem.util.JwtUtil;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.UserInfoVO;
import com.todo.todomanagementsystem.vo.UserListVO;
import com.todo.todomanagementsystem.vo.UserVO;
import org.springframework.beans.BeanUtils;
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
    private SysUserService sysUserService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Result<Page<UserListVO>> userPage(UserPageDTO dto) {
        Long userId = JwtConfig.currentUserId.get();
        User user = getById(userId);
        String username = user.getUsername();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();

        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException("无权限访问");
        }

        Page<SysUser> page = new Page<>(dto.getPage(), dto.getSize());

        Page<SysUser> sysUserPage = sysUserService.lambdaQuery()
                .like(dto.getUsername() != null && !dto.getUsername().isEmpty(), SysUser::getUsername, dto.getUsername())
                .like(dto.getNickname() != null && !dto.getNickname().isEmpty(), SysUser::getNickname, dto.getNickname())
                .eq(SysUser::getRole,dto.getRole())
                .page(page);

        Page<UserListVO> resultPage = (Page<UserListVO>) sysUserPage.convert(item -> {
            UserListVO vo = new UserListVO();
            BeanUtils.copyProperties(item, vo);
            if (item.getCreateTime() != null) {
                vo.setCreateTime(item.getCreateTime().toString());
            }
            return vo;
        });
        return Result.success(resultPage);
    }

    @Override
    public Result<?> logout() {
        return Result.success("退出成功");
    }

    @Override
    public Result<?> updateUser(String nickname, String phone, String password) {
        Long userId = JwtConfig.currentUserId.get();
        User user = getById(userId);
        String username = user.getUsername();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        sysUser.setNickname(nickname);
        sysUser.setPhone(phone);
        sysUser.setPassword(password);
        sysUser.setUpdateTime(new Date());
        user.setPassword(password);
        updateById(user);
        sysUserService.updateById(sysUser);

        return Result.success("修改成功");
    }

    @Override
    public Result<?> getUserInfo() {
        Long userId = JwtConfig.currentUserId.get();
        User user = getById(userId);
        String username = user.getUsername();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(sysUser,userInfoVO);
        return Result.success(userInfoVO);
    }

    @Override
    public Result<?> login(String username, String password) {
        UserVO userVO = new UserVO();
//        查询用户
        User user = lambdaQuery().eq(User::getUsername, username)
                .one();
        if (user == null){
            throw new BusinessException("用户名不存在");
        }
        if (!(user.getPassword().equals(password))){
            throw new BusinessException("密码错误");
        }
//查询sys
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if(sysUser != null){
            BeanUtils.copyProperties(sysUser,userVO);
        }
        else {
            BeanUtils.copyProperties(user,userVO);
        }

//         生成jwt令牌
        String token = jwtUtil.creatToken(user.getId());

//        封装返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
//         密码脱敏返回
        user.setPassword(null);
        map.put("user",userVO);
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