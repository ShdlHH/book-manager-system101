package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.config.JwtConfig;
import com.todo.todomanagementsystem.dto.UserAddDTO;
import com.todo.todomanagementsystem.dto.UserPageDTO;
import com.todo.todomanagementsystem.dto.UserStatusDTO;
import com.todo.todomanagementsystem.dto.UserUpdateDTO;
import com.todo.todomanagementsystem.entity.BorrowRecord;
import com.todo.todomanagementsystem.entity.SysUser;
import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.mapper.SysUserMapper;
import com.todo.todomanagementsystem.service.BorrowRecordService;
import com.todo.todomanagementsystem.service.UserService;
import com.todo.todomanagementsystem.util.JwtUtil;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.UserInfoVO;
import com.todo.todomanagementsystem.vo.UserListVO;
import com.todo.todomanagementsystem.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private BorrowRecordService borrowRecordService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    @Override
    public Result<?> deleteUser(Long id) {
        String username = JwtConfig.currentUserName.get();
        SysUser currentUser = lambdaQuery().eq(SysUser::getUsername, username).one();
        if (currentUser == null || currentUser.getRole() != 1) {
            throw new BusinessException("无权限操作");
        }

        SysUser sysUser = getById(id);
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (sysUser.getId().equals(currentUser.getId())) {
            throw new BusinessException("不能删除自己的账号");
        }

        long borrowCount = borrowRecordService.lambdaQuery()
                .eq(BorrowRecord::getUserId, id)
                .in(BorrowRecord::getStatus, 0, 3)
                .count();
        if (borrowCount > 0) {
            throw new BusinessException("该用户存在未归还的借阅记录，无法删除");
        }

        removeById(id);
        return Result.success("删除成功");
    }

    @Override
    public Result<?> updateUserStatus(Long id, UserStatusDTO dto) {
        String username = JwtConfig.currentUserName.get();
        SysUser currentUser = lambdaQuery().eq(SysUser::getUsername, username).one();
        if (currentUser == null || currentUser.getRole() != 1) {
            throw new BusinessException("无权限操作");
        }

        SysUser sysUser = getById(id);
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (dto.getStatus() != null && (dto.getStatus() == 0 || dto.getStatus() == 1)) {
            sysUser.setStatus(dto.getStatus());
            sysUser.setUpdateTime(new Date());
            updateById(sysUser);
        } else {
            throw new BusinessException("无效的状态值，只能为0或1");
        }

        return Result.success(dto.getStatus() == 1 ? "用户已启用" : "用户已禁用");
    }

    @Transactional
    @Override
    public Result<?> updateUserByAdmin(Long id, UserUpdateDTO dto) {
        String username = JwtConfig.currentUserName.get();
        SysUser currentUser = lambdaQuery().eq(SysUser::getUsername, username).one();
        if (currentUser == null || currentUser.getRole() != 1) {
            throw new BusinessException("无权限操作");
        }

        SysUser sysUser = getById(id);
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (dto.getNickname() != null) {
            sysUser.setNickname(dto.getNickname());
        }
        if (dto.getRole() != null) {
            sysUser.setRole(dto.getRole());
        }
        if (dto.getPhone() != null) {
            sysUser.setPhone(dto.getPhone());
        }
        if (dto.getMaxBorrowNum() != null) {
            sysUser.setMaxBorrowNum(dto.getMaxBorrowNum());
        }
        sysUser.setUpdateTime(new Date());

        updateById(sysUser);
        return Result.success("修改用户成功");
    }

    @Transactional
    @Override
    public Result<?> addUser(UserAddDTO dto) {
        String username = JwtConfig.currentUserName.get();
        SysUser currentUser = lambdaQuery().eq(SysUser::getUsername, username).one();
        if (currentUser == null || currentUser.getRole() != 1) {
            throw new BusinessException("无权限操作");
        }

        boolean exists = lambdaQuery().eq(SysUser::getUsername, dto.getUsername()).exists();
        if (exists) {
            throw new BusinessException("用户名已存在");
        }

        SysUser sysUser = new SysUser();
        sysUser.setUsername(dto.getUsername());
        sysUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        sysUser.setNickname(dto.getNickname());
        sysUser.setRole(dto.getRole());
        sysUser.setPhone(dto.getPhone());
        sysUser.setMaxBorrowNum(dto.getMaxBorrowNum());
        sysUser.setStatus(1);
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(new Date());
        save(sysUser);

        return Result.success("新增用户成功");
    }

    @Override
    public Result<Page<UserListVO>> userPage(UserPageDTO dto) {
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = lambdaQuery().eq(SysUser::getUsername, username).one();

        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException("无权限访问");
        }

        Page<SysUser> page = new Page<>(dto.getPage(), dto.getSize());

        Page<SysUser> sysUserPage = lambdaQuery()
                .like(dto.getUsername() != null && !dto.getUsername().isEmpty(), SysUser::getUsername, dto.getUsername())
                .like(dto.getNickname() != null && !dto.getNickname().isEmpty(), SysUser::getNickname, dto.getNickname())
                .eq(dto.getRole() != null, SysUser::getRole, dto.getRole())
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

    @Transactional
    @Override
    public Result<?> updateUser(String nickname, String phone, String password) {
        Long userId = JwtConfig.currentUserId.get();
        if (userId == null) {
            throw new BusinessException("用户未登录");
        }

        SysUser sysUser = getById(userId);
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (nickname != null) {
            sysUser.setNickname(nickname);
        }
        if (phone != null) {
            sysUser.setPhone(phone);
        }
        if (password != null && !password.isEmpty()) {
            sysUser.setPassword(passwordEncoder.encode(password));
        }
        sysUser.setUpdateTime(new Date());
        updateById(sysUser);

        return Result.success("修改成功");
    }

    @Override
    public Result<?> getUserInfo() {
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = lambdaQuery().eq(SysUser::getUsername, username).one();
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(sysUser, userInfoVO);
        return Result.success(userInfoVO);
    }

    @Override
    public Result<?> login(String username, String password) {
        SysUser sysUser = lambdaQuery().eq(SysUser::getUsername, username).one();

        if (sysUser == null) {
            throw new BusinessException("用户名不存在");
        }

        if (!passwordEncoder.matches(password, sysUser.getPassword())) {
            throw new BusinessException("密码错误");
        }

        if (sysUser.getStatus() != 1) {
            throw new BusinessException("用户已被禁用");
        }

        String token = jwtUtil.creatToken(sysUser.getId());

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser, userVO);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", userVO);

        return Result.success(map);
    }

    @Transactional
    @Override
    public Result<?> register(String username, String password) {
        boolean exists = lambdaQuery().eq(SysUser::getUsername, username).exists();

        if (!exists) {
            SysUser sysUser = new SysUser();
            sysUser.setUsername(username);
            sysUser.setPassword(passwordEncoder.encode(password));
            sysUser.setNickname(username);
            sysUser.setRole(0);
            sysUser.setStatus(1);
            sysUser.setMaxBorrowNum(5);
            sysUser.setCreateTime(new Date());
            sysUser.setUpdateTime(new Date());
            save(sysUser);
        }

        return Result.success("注册成功");
    }
}