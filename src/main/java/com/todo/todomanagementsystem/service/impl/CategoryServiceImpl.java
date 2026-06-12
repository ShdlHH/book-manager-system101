package com.todo.todomanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.todomanagementsystem.config.JwtConfig;
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

/**
* @author shdl
* @description 针对表【category】的数据库操作Service实现
* @createDate 2026-06-11 20:35:01
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private SysUserService sysUserService;

    // 添加 BookService 依赖
    @Autowired
    private BookService bookService;

    @Override
    public Result<?> deleteCategory(Long id) {
        // 1. 验证管理员权限
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException("无权限访问");
        }

        // 2. 检查分类是否存在
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 3. 检查是否有关联图书
        boolean hasBooks = bookService.lambdaQuery()
                .eq(Book::getCategoryId, id)
                .exists();
        if (hasBooks) {
            throw new BusinessException("该分类下存在图书，无法删除");
        }

        // 4. 删除分类
        removeById(id);

        return Result.success("删除成功");
    }

    @Override
    public Result<?> updateCategory(Long id, CategoryUpdateDTO dto) {
        // 1. 验证管理员权限
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException("无权限访问");
        }

        // 2. 检查分类是否存在
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 3. 如果修改名称，检查新名称是否已被使用
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

        // 4. 更新排序号
        if (dto.getSort() != null) {
            category.setSort(dto.getSort());
        }

        // 5. 保存修改
        updateById(category);

        return Result.success("修改分类成功");
    }

    @Override
    public Result<?> addCategory(CategoryAddDTO dto) {
        // 1. 验证管理员权限
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException("无权限访问");
        }

        // 2. 检查分类名称是否已存在
        boolean exists = lambdaQuery().eq(Category::getName, dto.getName()).exists();
        if (exists) {
            throw new BusinessException("分类名称已存在");
        }

        // 3. 创建分类
        Category category = new Category();
        category.setName(dto.getName());
        category.setSort(dto.getSort() != null ? dto.getSort() : 0);
        category.setCreateTime(new Date());

        // 4. 保存
        save(category);

        return Result.success("新增分类成功");
    }
    @Override
    public Result<?> getCategoryListPage(CategoryPageDTO dto) {
        // 1. 验证管理员权限
        String username = JwtConfig.currentUserName.get();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (sysUser == null || sysUser.getRole() != 1) {
            throw new BusinessException("无权限访问");
        }

        // 2. 构建分页查询
        Page<Category> page = new Page<>(dto.getPage(), dto.getSize());

        // 3. 执行查询（支持名称模糊搜索）
        Page<Category> categoryPage = lambdaQuery()
                .like(dto.getName() != null && !dto.getName().isEmpty(), Category::getName, dto.getName())
                .orderByAsc(Category::getSort)
                .page(page);

        // 4. 返回结果
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




