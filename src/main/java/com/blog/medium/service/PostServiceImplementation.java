package com.blog.medium.service;
import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import com.blog.medium.repository.CategoryRepository;
import com.blog.medium.repository.PostRepository;

import com.blog.medium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service("PostService")
@Transactional
public class PostServiceImplementation implements PostService {

    @Autowired
    private PostRepository<Post> postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Post> getAllPosts(){
//        return (List<Post>) postRepository.findAllById(Collections.singleton(2L));
        return postRepository.findAllByOrderByIdAsc();
    }

    public List<Post> getAllPostsSortedByPublishDate() {
        return postRepository.findAllByOrderByPublishedAtDesc();
    }

    public List<Post> getAllPostsSortedByLastUpdate() {
        return postRepository.findAllByOrderByUpdateDateTimeDesc();
    }


    public Post getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        Post p = post.get();
        Set<Category> categories = p.getCategories();
        for (Category category : categories) {
            System.out.println("CATEGORY ======= " + category.toString());
        }
        return post.isPresent() ? post.get() :null;
    }

    public  Long addPost(String title, String content, List<String> categories) {

/*            User user = new User();
            user.setEmail("admin@admin.com");
            user.setUsername("admin");
            user.setPassword("admin");*/
        Optional<User> userOptional = userRepository.findById(1L);
        User user = userOptional.get();
        Post post = new Post();
        post.setPublishedAt(LocalDateTime.now());
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);

        for(String category : categories) {
            Category categoryFounds = categoryRepository.findByCategoryName(category);
            Category categoryFound = categoryFounds;
            categoryFound.getPosts().add(post);
            post.getCategories().add(categoryFound);
        }

        user.getPosts().add(post);
        userRepository.save(user);
        Long id = postRepository.save(post).getId();
        return id;
    }

    public  void deletePost(Long id){
        postRepository.deleteById(id);
    }

    public  void updatePost(Long id, String title, String content, List<String> categoriesList) {

        Post postFromDB = postRepository.findById(id).get();

//        User user = userRepository.findById(1L).get();
//        user.getPosts().remove(postFromDB);

        postFromDB.setContent(content);
        postFromDB.setTitle(title);

        for(String categoryName: categoriesList) {
            Category category =  categoryRepository.findByCategoryName(categoryName);

            if(!(postFromDB.getCategories().contains(category))){
                postFromDB.getCategories().add(category);
                category.getPosts().add(postFromDB);
            }
        }

        if(postFromDB.getCategories().size() > 1) {
            Category uncategorizedCategory = categoryRepository.findByCategoryName("uncategorized");
            if(postFromDB.getCategories().contains(uncategorizedCategory)){
                postFromDB.getCategories().remove(uncategorizedCategory);
                uncategorizedCategory.getPosts().remove(postFromDB);
            }
        }


//        user.getPosts().add(postFromDB);
//        Post post = new Post();
//        post.setContent(content);
//        post.setId(id);
//        post.setTitle(title);
        postRepository.save(postFromDB);

    }

    public List<Post> findJsonDataByCondition(String orderBy, String direction, int page, int size) {
        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> data = postRepository.findAll(pageable);
        System.out.println("PADASDASD DATAAA = " + data.getContent());
        return data.getContent();
    }

    @Override
    public List<Post> search(String keyword) {
        return null;
    }


}
