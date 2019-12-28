package com.blog.medium.service;
import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import com.blog.medium.repository.CategoryRepository;
import com.blog.medium.repository.PostRepository;

import com.blog.medium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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


    /*
    * Filter service implementations
    * */

    public Page<Post> search(String titleWord, String contentWord, String categoryWord, String orderBy,String direction,Integer pageNo, Integer pageSize) {

        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return postRepository.findDistinctByTitleContainingOrContentContainingOrCategories_categoryNameContains(titleWord, contentWord, categoryWord,pageable);
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

    @Override
    public Page<Post> findAllByOrderBy(String orderBy, String direction, Integer page, Integer size) {
        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> data = postRepository.findAll(pageable);
        System.out.println("order, direction, page, size = " + orderBy + " " + direction + " " + page + " " + size);
        System.out.println("PADASDASD DATAAA = " + data.getContent());
        System.out.println("DATA CONTENT SIZE testing + " + data.getContent().size());
        return data;
    }



    public Page<Post> filterPostsMethod(String username, String tagName, String orderBy, String direction, String operation, String searchQuery, String page, String size){
        Integer pageNo = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(size);

        Page data = null;
        if(!(searchQuery.equals(""))){
            data = search(searchQuery,searchQuery,searchQuery,orderBy,direction,pageNo,pageSize);
        }
        else if(tagName.contains(",") && !(username.equals("noUser"))) {
            //Username with multiple categories.
            if(operation.toLowerCase().equals("or")) {
                data = getPostsWithUsernameAndMultipleTagsOrOperation(username, tagName, orderBy, direction, pageNo, pageSize);
            }
            else{
                data = getPostsWithUsernameAndMultipleTagsAndOperation(username, tagName, orderBy, direction, pageNo, pageSize);
            }
        }
        else if(tagName!= null && !(tagName.toLowerCase().equals("notag")) && !(username.equals("noUser"))){
            //Username with single category.
            data = getPostsWithUsernameAndTag(username,tagName,orderBy,direction,pageNo,pageSize);
        }
        else if(tagName.contains(",")){
            String [] categories = tagName.split(",");
            if(operation.toLowerCase().equals("and")) {
                data = getPostsByMultipleTags(categories, orderBy, direction, pageNo, pageSize);
            }
            else if(operation.toLowerCase().equals("or")){
                Pageable pageable = PageRequest.of(pageNo, pageSize);
                data = postRepository.findDistinctByCategories_categoryNameIn(categories, pageable);
            }
        }
        else if(!(username.equals("noUser"))) {
            System.out.println("BY USERNAME");
            data =  getBlogPostsByUser(username, orderBy, direction, page, size);
        }

        else if( tagName!= null && !(tagName.toLowerCase().equals("notag"))){
            System.out.println("BY TAG NAME ");
            data  = findDataByTagNameOrderBy(tagName, orderBy, direction, pageNo, pageSize);

        }else{
            System.out.println("BY ALL POSTS");
            data = findAllByOrderBy(orderBy, direction, pageNo, pageSize);
        }
        return data;
    }

    private Page<Post> getPostsWithUsernameAndTag(String username, String tagName, String orderBy, String direction, Integer pageNo, Integer pageSize){
        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        return postRepository.findAllByUser_usernameAndCategories_categoryNameContains(username,tagName,pageable);
    }

    private Page<Post> getPostsWithUsernameAndMultipleTagsOrOperation(String username, String tagName, String orderBy, String direction, Integer pageNo, Integer pageSize){
        String[] categories = tagName.split(",");

        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        return postRepository.findAllByUser_usernameAndCategories_categoryNameIn(username,categories,pageable);
    }

    private Page<Post> getPostsWithUsernameAndMultipleTagsAndOperation(String username, String tagName, String orderBy, String direction, Integer pageNo, Integer pageSize){
        String[] categories = tagName.split(",");

        List<Post> allPosts = postRepository.findAll();
        List<Category> categoryList = new ArrayList<>();
        for(String categoryName: categories) {
            Category category = categoryRepository.findByCategoryName(categoryName);
            categoryList.add(category);
        }

        List<Post> allPostsWithAllCategories = new ArrayList<>();
        for(Post post: allPosts){
            if(post.getCategories().containsAll(categoryList) && post.getUser().getUsername().equals(username)){
                allPostsWithAllCategories.add(post);
            }
        }

        long start =  PageRequest.of(pageNo, pageSize).getOffset();
        long end = (start + PageRequest.of(pageNo, pageSize).getPageSize()) > allPostsWithAllCategories.size() ? allPostsWithAllCategories.size() : (start + PageRequest.of(pageNo, pageSize).getPageSize());
        return new PageImpl<Post>(allPostsWithAllCategories.subList((int) start,(int) end),PageRequest.of(pageNo,pageSize),allPostsWithAllCategories.size());


    }

    @Override
    public Page<Post> getBlogPostsByUser(String userName, String orderBy, String direction, String page, String size) {

        Integer pageNo = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(size);

        User user = userRepository.findUserByUsername(userName);
        List<Post> userPosts = user.getPosts();

        if (direction.equals("ASC")) {
//            userPosts.sort(Comparator.comparing(Post::getCreateDateTime));
            if (orderBy.equals("CreateDateTime")) {
                userPosts.sort(Comparator.comparing(Post::getCreateDateTime));
            } else {
                userPosts.sort(Comparator.comparing(Post::getUpdateDateTime));
            }
            ;
        }
        if (direction.equals("DESC")) {
            if (orderBy.equals("CreateDateTime")) {
                userPosts.sort((Post s1, Post s2) -> s2.getCreateDateTime().compareTo(s1.getCreateDateTime()));
            } else {
                userPosts.sort((Post s1, Post s2) -> s2.getUpdateDateTime().compareTo(s1.getUpdateDateTime()));
            }
        }
        long start =  PageRequest.of(pageNo, pageSize).getOffset();
        long end = (start + PageRequest.of(pageNo, pageSize).getPageSize()) > userPosts.size() ? userPosts.size() : (start + PageRequest.of(pageNo, pageSize).getPageSize());
        return new PageImpl<Post>(userPosts.subList((int) start,(int) end),PageRequest.of(pageNo,pageSize),userPosts.size());

    }

    public Page<Post> getfilterPostsHomeMethod(String page, String size) {
        Integer pageNo = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(size);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page data = postRepository.findAllByOrderByIdAsc(pageable);

        return data;
    }

    @Override
    public Page<Post> getPostsByMultipleTags(String[] categories, String orderBy, String direction, Integer pageNo, Integer pageSize) {

        List<Post> allPosts = postRepository.findAll();
        List<Category> categoryList = new ArrayList<>();
        for(String categoryName: categories) {
            Category category = categoryRepository.findByCategoryName(categoryName);
            categoryList.add(category);
        }

        List<Post> allPostsWithAllCategories = new ArrayList<>();
        for(Post post: allPosts){
            if(post.getCategories().containsAll(categoryList)){
                allPostsWithAllCategories.add(post);
            }
        }

        long start =  PageRequest.of(pageNo, pageSize).getOffset();
        long end = (start + PageRequest.of(pageNo, pageSize).getPageSize()) > allPostsWithAllCategories.size() ? allPostsWithAllCategories.size() : (start + PageRequest.of(pageNo, pageSize).getPageSize());
        return new PageImpl<Post>(allPostsWithAllCategories.subList((int) start,(int) end),PageRequest.of(pageNo,pageSize),allPostsWithAllCategories.size());


    }

}
