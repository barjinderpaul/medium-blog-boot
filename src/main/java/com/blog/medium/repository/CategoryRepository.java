package com.blog.medium.repository;

import com.blog.medium.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("select u from Category u where u.categoryName = ?1")
    List<Category> findByName(String s);

    Page<Category> findByCategoryName(String name, Pageable pageable);
//     List<Category> findByName(String s);
//    Page<Category> findAllByCategory_id(Long id, Pageable pageable);
}

