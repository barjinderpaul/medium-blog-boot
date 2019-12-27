package com.blog.medium.service;

import com.blog.medium.model.Post;

import java.util.List;
import java.util.Set;

public interface FilterService {
    Set<Post> search(String word, String word2);
    Set<Post> getFromCategories(String word);
}
