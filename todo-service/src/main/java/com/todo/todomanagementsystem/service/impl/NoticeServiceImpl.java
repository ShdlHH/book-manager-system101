package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.entity.Notice;
import com.todo.todomanagementsystem.service.NoticeService;
import com.todo.todomanagementsystem.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author shdl
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2026-06-11 20:35:09
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

}




