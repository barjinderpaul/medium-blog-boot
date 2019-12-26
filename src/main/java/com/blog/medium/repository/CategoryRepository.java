package com.blog.medium.repository;

import com.blog.medium.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Long> {

    @Query("select u from Category u where u.categoryName = ?1")
    List<Category> findByName(String s);
//     List<Category> findByName(String s);
}
