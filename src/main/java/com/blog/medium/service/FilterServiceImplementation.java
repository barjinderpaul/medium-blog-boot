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
    public List<Post> findDataByTagName(String tagName, String orderBy, String direction, Integer pageNo, Integer size) {
        Category category = categoryRepository.findByCategoryName(tagName);

        Set<Post> postCategory = category.getPosts();
        System.out.println("SET POST = ");
        for(Post post : postCategory) {
            System.out.println(post.getTitle());
        }
        List<Post> listCategory = new ArrayList<>();
        listCategory.addAll(postCategory);

        System.out.println("LIST POST = ");
        for (Post post: listCategory){
            System.out.println(post.getTitle());
        }

        List<Post> sortedByOrder;
        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
            listCategory.sort(Comparator.comparing(Post::getUpdateDateTime));
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
            listCategory.sort((Post s1, Post s2)-> s2.getUpdateDateTime().compareTo(s1.getUpdateDateTime()));
        }
        assert sort != null;
        long start =  PageRequest.of(pageNo, size).getOffset();
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
