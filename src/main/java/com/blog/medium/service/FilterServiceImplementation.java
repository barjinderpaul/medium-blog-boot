package com.blog.medium.service;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.repository.CategoryRepository;
import com.blog.medium.repository.FilterRepository;
import com.blog.medium.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class FilterServiceImplementation implements FilterService {

    @Autowired
    FilterRepository filterRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Set<Post> search(String word, String word2) {
       Set<Post> searchResults = filterRepository.findAllByTitleContainingOrContentContaining(word,word);
       return searchResults;
    }

    @Override
    public Set<Post> getFromCategories(String word) {
        word = word.toLowerCase();
        Category toSearchCategory = categoryRepository.findByName(word).get(0);

        List<Post> allPosts = postRepository.findAll();
        Set<Post> matchedPosts = new LinkedHashSet<>();
        for(Post post : allPosts){
            if(post.getCategories().contains(toSearchCategory)){
                matchedPosts.add(post);
            }
        }

        return matchedPosts;
    }


}
