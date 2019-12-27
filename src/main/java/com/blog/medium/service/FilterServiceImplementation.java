package com.blog.medium.service;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.repository.CategoryRepository;
import com.blog.medium.repository.FilterRepository;
import com.blog.medium.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Category categoryListSingle = categoryRepository.findByCategoryName(word);

        if(categoryListSingle == null){
            return new HashSet<>();
        }

        Category toSearchCategory = categoryListSingle;

        List<Post> allPosts = postRepository.findAll();
        Set<Post> matchedPosts = new LinkedHashSet<>();
        for(Post post : allPosts){
            if(post.getCategories().contains(toSearchCategory)){
                matchedPosts.add(post);
            }
        }

        return matchedPosts;
    }

    @Override
    public Page<Post> findDataByTagNameOrderBy(String tagName, String orderBy, String direction, Integer pageNo, Integer size) {
        Category category = categoryRepository.findByCategoryName(tagName);

        Set<Post> postCategory = category.getPosts();
        System.out.println("SET POST = ");

        List<Post> listCategory = new ArrayList<>();
        listCategory.addAll(postCategory);

        if (direction.equals("ASC")) {
            listCategory.sort(Comparator.comparing(Post::getCreateDateTime));
            if (orderBy.equals("CreateDateTime")) {
                listCategory.sort(Comparator.comparing(Post::getCreateDateTime));
            } else {
                listCategory.sort(Comparator.comparing(Post::getUpdateDateTime));
            }
            ;
        }
        if (direction.equals("DESC")) {
            if (orderBy.equals("CreateDateTime")) {
                listCategory.sort((Post s1, Post s2) -> s2.getCreateDateTime().compareTo(s1.getCreateDateTime()));
            } else {
                listCategory.sort((Post s1, Post s2) -> s2.getUpdateDateTime().compareTo(s1.getUpdateDateTime()));
            }
        }
        long start =  PageRequest.of(pageNo, size).getOffset();
        long end = (start + PageRequest.of(pageNo, size).getPageSize()) > listCategory.size() ? listCategory.size() : (start + PageRequest.of(pageNo, size).getPageSize());
        return new PageImpl<Post>(listCategory.subList((int) start,(int) end),PageRequest.of(pageNo,size),listCategory.size());

    }

}
