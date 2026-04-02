package com.calligraphy.service.impl;

import com.calligraphy.entity.Category;
import com.calligraphy.mapper.CategoryMapper;
import com.calligraphy.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> listAll() {
        return categoryMapper.selectList(null);
    }
}