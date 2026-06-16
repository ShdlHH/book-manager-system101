package com.todo.todomanagementsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.todomanagementsystem.config.JwtConfig;
import com.todo.todomanagementsystem.dto.CategoryAddDTO;
import com.todo.todomanagementsystem.dto.CategoryPageDTO;
import com.todo.todomanagementsystem.dto.CategoryUpdateDTO;
import com.todo.todomanagementsystem.entity.Category;
import com.todo.todomanagementsystem.entity.SysUser;
import com.todo.todomanagementsystem.exception.BusinessException;
import com.todo.todomanagementsystem.service.CategoryService;
import com.todo.todomanagementsystem.service.SysUserService;
import com.todo.todomanagementsystem.util.Result;
import com.todo.todomanagementsystem.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
@Tag(name = "分类管理", description = "图书分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private SysUserService sysUserService;

    @Operation(summary = "获取所有分类", description = "获取所有分类，用于下拉选择框")
    @GetMapping("/list")
    public Result<?> getCategoryList() {
        return categoryService.getCategoryList();
    }

    @Operation(summary = "分页查询分类", description = "分页查询分类（仅管理员）")
    @GetMapping("/page")
    public Result<?> getCategoryPage(CategoryPageDTO dto) {
        return categoryService.getCategoryListPage(dto);
    }

    @Operation(summary = "新增分类", description = "新增分类（仅管理员）")
    @PostMapping
    public Result<?> addCategory(@RequestBody CategoryAddDTO dto) {
        return categoryService.addCategory(dto);
    }

    @Operation(summary = "修改分类", description = "修改分类（仅管理员）")
    @PutMapping("/{id}")
    public Result<?> updateCategory(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @RequestBody CategoryUpdateDTO dto) {
        return categoryService.updateCategory(id, dto);
    }

    @Operation(summary = "删除分类", description = "删除分类（仅管理员）")
    @DeleteMapping("/{id}")
    public Result<?> deleteCategory(@Parameter(description = "分类ID") @PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}