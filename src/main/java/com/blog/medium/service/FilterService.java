package com.blog.medium.service;

import com.blog.medium.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

public interface FilterService {
    Set<Post> search(String word, String word2);
    Set<Post> getFromCategories(String word);
    ModelAndView filterPostsMethod(String tagName, String orderBy, String direction, String page, String size);
    Page<Post> findDataByTagNameOrderBy(String tagName, String orderBy, String direction, Integer pageNo, Integer size);
    Page<Post> findAllByOrderBy(String orderBy, String direction, Integer pageNo, Integer pageSize);
    Page<Post> getBlogPostsByUser(String userName,String orderBy,String direction,String page, String size);
}
