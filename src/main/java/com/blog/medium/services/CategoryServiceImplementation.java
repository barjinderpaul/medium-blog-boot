package com.blog.medium.services;

import com.blog.medium.exceptions.InvalidArgumentException;
import com.blog.medium.model.Category;
import com.blog.medium.repository.CategoryRepository;
import com.blog.medium.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public Long save(String categoryName) {
        if(categoryName == null) {
            throw new InvalidArgumentException("Category cannot be null");
        }
        Category isExistingCategory = categoryRepository.findByCategoryName(categoryName);
        if(isExistingCategory != null ){
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        Long id = categoryRepository.save(category).getCategory_id();
        return id;
    }
}
