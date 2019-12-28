package com.blog.medium.repository;

import com.blog.medium.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface PostRepository<P> extends JpaRepository<Post,Long> {
    List<P>  findAllByOrderByIdAsc();
//    Page<P> findAllByOrderByAsc(Pageable pageable);
    List<P> findAllByOrderByUpdateDateTimeDesc();
    List<P> findAllByOrderByPublishedAtDesc();
    Page<Post> findByCategories(String name, Pageable pageable);

}
