package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.config.JwtConfig;
import com.todo.todomanagementsystem.dto.BorrowAddDTO;
import com.todo.todomanagementsystem.dto.BorrowPageDTO;
import com.todo.todomanagementsystem.dto.BorrowVO;
import com.todo.todomanagementsystem.entity.Book;
import com.todo.todomanagementsystem.entity.BorrowRecord;
import com.todo.todomanagementsystem.entity.SysUser;
import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.service.BookService;
import com.todo.todomanagementsystem.service.BorrowRecordService;
import com.todo.todomanagementsystem.mapper.BorrowRecordMapper;
import com.todo.todomanagementsystem.service.UserService;
import com.todo.todomanagementsystem.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord>
        implements BorrowRecordService {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    private static final int MAX_BORROW_NUM = 5;
    private static final int MAX_RENEW_COUNT = 2;
    private static final int BORROW_DAYS = 14;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> borrowBook(BorrowAddDTO dto) {
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = userService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (sysUser.getStatus() != 1) {
            throw new BusinessException("用户已被禁用");
        }

        int maxBorrowNum = sysUser.getMaxBorrowNum() != null ? sysUser.getMaxBorrowNum() : MAX_BORROW_NUM;
        long borrowCount = lambdaQuery()
                .eq(BorrowRecord::getUserId, sysUser.getId())
                .in(BorrowRecord::getStatus, 0, 3)
                .count();
        if (borrowCount >= maxBorrowNum) {
            throw new BusinessException("借阅数量已达上限");
        }

        Date now = new Date();

        boolean updateSuccess = bookService.lambdaUpdate()
                .eq(Book::getId, dto.getBookId())
                .eq(Book::getStatus, 1)
                .gt(Book::getAvailableNum, 0)
                .setSql("available_num = available_num - 1")
                .set(Book::getUpdateTime, now)
                .update();

        if (!updateSuccess) {
            throw new BusinessException("图书库存不足或已下架");
        }

        BorrowRecord record = new BorrowRecord();
        record.setUserId(sysUser.getId());
        record.setBookId(dto.getBookId());
        record.setBorrowDate(now);
        record.setDueDate(new Date(now.getTime() + BORROW_DAYS * 24 * 60 * 60 * 1000L));
        record.setStatus(0);
        record.setRenewCount(0);
        record.setCreateTime(now);

        save(record);

        return Result.success("借阅成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> returnBook(Long id) {
        BorrowRecord record = getById(id);
        if (record == null) {
            throw new BusinessException("借阅记录不存在");
        }

        if (record.getStatus() == 1) {
            throw new BusinessException("图书已归还");
        }

        Date now = new Date();
        int status = now.after(record.getDueDate()) ? 2 : 1;

        boolean updateSuccess = bookService.lambdaUpdate()
                .eq(Book::getId, record.getBookId())
                .setSql("available_num = available_num + 1")
                .set(Book::getUpdateTime, now)
                .update();

        if (!updateSuccess) {
            throw new BusinessException("图书不存在");
        }

        lambdaUpdate()
                .eq(BorrowRecord::getId, id)
                .set(BorrowRecord::getReturnDate, now)
                .set(BorrowRecord::getStatus, status)
                .update();

        return Result.success("归还成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> renewBook(Long id) {
        BorrowRecord record = getById(id);
        if (record == null) {
            throw new BusinessException("借阅记录不存在");
        }

        if (record.getStatus() != 0 && record.getStatus() != 3) {
            throw new BusinessException("无法续借");
        }

        if (record.getRenewCount() >= MAX_RENEW_COUNT) {
            throw new BusinessException("续借次数已达上限");
        }

        record.setDueDate(new Date(record.getDueDate().getTime() + BORROW_DAYS * 24 * 60 * 60 * 1000L));
        record.setRenewCount(record.getRenewCount() + 1);
        record.setStatus(3);

        updateById(record);

        return Result.success("续借成功");
    }

    @Override
    public Result<Page<BorrowVO>> getMyBorrowList() {
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = userService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }

        Page<BorrowRecord> page = new Page<>(1, 50);
        Page<BorrowRecord> recordPage = lambdaQuery()
                .eq(BorrowRecord::getUserId, sysUser.getId())
                .orderByDesc(BorrowRecord::getCreateTime)
                .page(page);

        return Result.success(convertToVO(page, recordPage));
    }

    @Override
    public Result<Page<BorrowVO>> getBorrowPage(BorrowPageDTO dto) {
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = userService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException("无权限访问");
        }

        Page<BorrowRecord> page = new Page<>(dto.getPage(), dto.getSize());

        var query = lambdaQuery();

        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            List<SysUser> users = userService.lambdaQuery()
                    .like(SysUser::getUsername, dto.getUsername())
                    .list();
            if (!users.isEmpty()) {
                List<Long> userIds = users.stream().map(SysUser::getId).collect(Collectors.toList());
                query.in(BorrowRecord::getUserId, userIds);
            } else {
                query.eq(BorrowRecord::getUserId, -1);
            }
        }

        if (dto.getBookTitle() != null && !dto.getBookTitle().isEmpty()) {
            List<Book> books = bookService.lambdaQuery()
                    .like(Book::getTitle, dto.getBookTitle())
                    .list();
            if (!books.isEmpty()) {
                List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
                query.in(BorrowRecord::getBookId, bookIds);
            } else {
                query.eq(BorrowRecord::getBookId, -1);
            }
        }

        query.eq(dto.getStatus() != null, BorrowRecord::getStatus, dto.getStatus())
                .orderByDesc(BorrowRecord::getCreateTime);

        Page<BorrowRecord> recordPage = query.page(page);

        return Result.success(convertToVO(page, recordPage));
    }

    @Override
    public Result<Page<BorrowVO>> getOverdueList(Integer page, Integer size) {
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = userService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException("无权限访问");
        }

        Page<BorrowRecord> pageObj = new Page<>(page, size);
        Date now = new Date();

        Page<BorrowRecord> recordPage = lambdaQuery()
                .in(BorrowRecord::getStatus, 0, 3)
                .lt(BorrowRecord::getDueDate, now)
                .orderByDesc(BorrowRecord::getDueDate)
                .page(pageObj);

        return Result.success(convertToVO(pageObj, recordPage));
    }

    private Page<BorrowVO> convertToVO(Page<BorrowRecord> page, Page<BorrowRecord> recordPage) {
        Page<BorrowVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(recordPage.getRecords().stream().map(record -> {
            BorrowVO vo = BorrowVO.from(record);

            SysUser sysUser = userService.getById(record.getUserId());
            if (sysUser != null) {
                vo.setUsername(sysUser.getUsername());
                vo.setPhone(sysUser.getPhone());
            }

            Book book = bookService.getById(record.getBookId());
            if (book != null) {
                vo.setBookTitle(book.getTitle());
                vo.setAuthor(book.getAuthor());
            }

            Date now = new Date();
            if (record.getStatus() != 1 && record.getDueDate() != null && now.after(record.getDueDate())) {
                vo.setOverdueDays((int) ((now.getTime() - record.getDueDate().getTime()) / (24 * 60 * 60 * 1000)));
            }

            return vo;
        }).toList());
        return voPage;
    }
}