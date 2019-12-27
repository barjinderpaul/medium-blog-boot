package com.blog.medium.service;

import com.blog.medium.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPost(Long id);
    Long addPost(String title, String content, List<String> categoriesList);
    void deletePost(Long id);
    void updatePost(Long id, String title, String content, List<String> categoriesList);
    List<Post> getAllPostsSortedByPublishDate();
    List<Post> getAllPostsSortedByLastUpdate();
    List<Post> findJsonDataByCondition(String orderBy, String direction, int page, int size);
    List<Post> search(String keyword);
}
