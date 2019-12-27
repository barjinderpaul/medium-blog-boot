package com.blog.medium.repository;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FilterRepository extends JpaRepository<Post,Long> {
    Set<Post> findAllByTitleContainingOrContentContaining(String word, String word2);

//    @Query("Select categories from User categories where  LIKE  %?1%")

//    @Query("Select cate from User users where ?1 member of categories")

    //    @Query("Select c from Category c where ?1 IN (c.catego)")


//    List<Post> getFromCategories(String word);


/*
    @Query("SELECT u FROM User u WHERE u.posts IN (:word)")
    List<Post> getFromCategories(String word);
*/

 /*   @Query("Select categories from Category categories where ?1 member categories.posts")
    List<Category> getTags(String word);*/
}
