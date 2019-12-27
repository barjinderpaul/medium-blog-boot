package com.blog.medium.service;

import com.blog.medium.model.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPost(Long id);
    Long addPost(String title, String content, List<String> categoriesList);
    void deletePost(Long id);
    void updatePost(Long id, String title, String content, List<String> categoriesList);
    List<Post> getAllPostsSortedByPublishDate();
    List<Post> getAllPostsSortedByLastUpdate();
}
