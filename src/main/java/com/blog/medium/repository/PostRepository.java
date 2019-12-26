package com.blog.medium.repository;

import com.blog.medium.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository<P> extends CrudRepository<Post,Long> {
    List<P>  findAllByOrderByIdAsc();
    List<P> findAllByOrderByUpdateDateTimeDesc();
    List<P> findAllByOrderByPublishedAtDesc();
}
