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
        List<Category> categoryListSingle = categoryRepository.findByName(word);

        if(categoryListSingle == null || categoryListSingle.size() == 0){
            return new HashSet<>();
        }

        Category toSearchCategory = categoryListSingle.get(0);

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
    public List<Post> findDataByTagName(String tagName, String orderBy, String direction, Integer pageNo, Integer size) {
        Category category = categoryRepository.findByName(tagName).get(0);

        Set<Post> postCategory = category.getPosts();
        List<Post> listCategory = new ArrayList<>();
        listCategory.addAll(postCategory);

        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        long start =  PageRequest.of(pageNo, size, sort).getOffset();
        long end = (start + PageRequest.of(pageNo, size).getPageSize()) > listCategory.size() ? listCategory.size() : (start + PageRequest.of(pageNo, size).getPageSize());

        System.out.println("PAGE CATEGORY = " + listCategory.size());

        return new PageImpl<Post>(listCategory.subList((int) start,(int) end),PageRequest.of(pageNo,size),listCategory.size()).getContent();

        /*
        * Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> data = postRepository.findAll(pageable);
        System.out.println("PADASDASD DATAAA = " + data.getContent());
        return data.getContent();
        * */

        /*
        * List<Patient> patientsList = new ArrayList<Patient>();
        Set<Patient> list=searchPatient(patient);
        patientsList.addAll(list);
        int start =  new PageRequest(page, size).getOffset();
        int end = (start + new PageRequest(page, size).getPageSize()) > patientsList.size() ? patientsList.size() : (start + new PageRequest(page, size).getPageSize());

        return new PageImpl<Patient>(patientsList.subList(start, end), new PageRequest(page, size), patientsList.size());

        *
        * */

//        return new ArrayList<>();

    }


}
