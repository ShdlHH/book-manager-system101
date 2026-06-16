package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.util.ThreadLocalContext;
import com.todo.todomanagementsystem.dto.CategoryAddDTO;
import com.todo.todomanagementsystem.dto.CategoryPageDTO;
import com.todo.todomanagementsystem.dto.CategoryUpdateDTO;
import com.todo.todomanagementsystem.entity.Book;
import com.todo.todomanagementsystem.entity.Category;
import com.todo.todomanagementsystem.entity.SysUser;
import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.service.BookService;
import com.todo.todomanagementsystem.service.CategoryService;
import com.todo.todomanagementsystem.mapper.CategoryMapper;
import com.todo.todomanagementsystem.service.SysUserService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private BookService bookService;

    @Override
    public Result<?> deleteCategory(Long id) {
        String username = ThreadLocalContext.getCurrentUserName();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException(403, "无权限操作");
        }

        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        boolean hasBooks = bookService.lambdaQuery()
                .eq(Book::getCategoryId, id)
                .exists();
        if (hasBooks) {
            throw new BusinessException("该分类下存在图书，无法删除");
        }

        removeById(id);
        return Result.success("删除成功");
    }

    @Override
    public Result<?> updateCategory(Long id, CategoryUpdateDTO dto) {
        String username = ThreadLocalContext.getCurrentUserName();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException(403, "无权限操作");
        }

        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            boolean exists = lambdaQuery()
                    .eq(Category::getName, dto.getName())
                    .ne(Category::getId, id)
                    .exists();
            if (exists) {
                throw new BusinessException("分类名称已存在");
            }
            category.setName(dto.getName());
        }

        if (dto.getSort() != null) {
            category.setSort(dto.getSort());
        }

        updateById(category);
        return Result.success("修改分类成功");
    }

    @Override
    public Result<?> addCategory(CategoryAddDTO dto) {
        String username = ThreadLocalContext.getCurrentUserName();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException(403, "无权限操作");
        }

        boolean exists = lambdaQuery().eq(Category::getName, dto.getName()).exists();
        if (exists) {
            throw new BusinessException("分类名称已存在");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setSort(dto.getSort() != null ? dto.getSort() : 0);
        category.setCreateTime(new Date());

        save(category);
        return Result.success("新增分类成功");
    }

    @Override
    public Result<?> getCategoryListPage(CategoryPageDTO dto) {
        String username = ThreadLocalContext.getCurrentUserName();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException(403, "无权限操作");
        }

        Page<Category> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<Category> categoryPage = lambdaQuery()
                .like(dto.getName() != null && !dto.getName().isEmpty(), Category::getName, dto.getName())
                .orderByAsc(Category::getSort)
                .page(page);

        return Result.success(categoryPage);
    }

    @Override
    public Result<?> getCategoryList() {
        List<Category> categories = list();
        List<CategoryVO> result = categories.stream()
                .map(cat -> new CategoryVO(cat.getId(), cat.getName()))
                .collect(Collectors.toList());
        return Result.success(result);
    }
}
