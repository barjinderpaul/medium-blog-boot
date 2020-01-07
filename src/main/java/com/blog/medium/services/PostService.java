package com.blog.medium.services;

import com.blog.medium.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    Post getPost(String id);
    Long addPost(String title, String content, List<String> categoriesList);
    void deletePost(Long id);
    Long updatePost(String id, String title, String content, List<String> categoriesList);

    Page<Post> filterPostsWithoutSearch(String username, String tagName, String orderBy, String direction, String operation, String page, String size);

    Page<Post> filterPostsBySearch(String username, String tagName, String orderBy, String direction, String operation, String searchQuery, String page, String size);

    Page<Post> getPostsByMultipleTags(String[] tags, String orderBy, String direction, Integer pageNo, Integer pageSize);

    Page<Post> getAllPostsHome(String page, String pageSize, String orderBy, String direction);
}
