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
}
