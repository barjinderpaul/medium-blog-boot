package com.blog.medium.service;

import com.blog.medium.model.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    List<Category> getAllTags();
    Category getCategory(Long id);
}
