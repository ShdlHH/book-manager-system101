package com.todo.todomanagementsystem.service;

import com.todo.todomanagementsystem.dto.CategoryAddDTO;
import com.todo.todomanagementsystem.dto.CategoryPageDTO;
import com.todo.todomanagementsystem.dto.CategoryUpdateDTO;
import com.todo.todomanagementsystem.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.todomanagementsystem.util.Result;

/**
* @author shdl
* @description 针对表【category】的数据库操作Service
* @createDate 2026-06-11 20:35:01
*/
public interface CategoryService extends IService<Category> {

    Result<?> getCategoryList();

    Result<?> getCategoryListPage(CategoryPageDTO dto);

    Result<?> addCategory(CategoryAddDTO dto);

    Result<?> updateCategory(Long id, CategoryUpdateDTO dto);

    Result<?> deleteCategory(Long id);
}
