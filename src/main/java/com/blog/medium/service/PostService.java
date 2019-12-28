package com.blog.medium.service;

import com.blog.medium.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

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

//    FilterServiceInterface
    Page<Post> search(String titleQuery, String contentQuery, String categoryQuery,String orderBy,String direction,Integer pageNo, Integer pageSize);
    Set<Post> getFromCategories(String word);
    Page<Post> filterPostsMethod(String username, String tagName, String orderBy, String direction, String operation,String searchQuery, String page, String size);
    Page<Post> findDataByTagNameOrderBy(String tagName, String orderBy, String direction, Integer pageNo, Integer size);
    Page<Post> findAllByOrderBy(String orderBy, String direction, Integer pageNo, Integer pageSize);
    Page<Post> getBlogPostsByUser(String userName,String orderBy,String direction,String page, String size);
    Page<Post> getfilterPostsHomeMethod(String page, String size);

    Page<Post> getPostsByMultipleTags(String[] tags, String orderBy, String direction, Integer pageNo, Integer pageSize);

}
