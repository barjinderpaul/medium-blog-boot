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
        return postRepository.findAllByOrderByIdAsc();
    }

    public List<Post> getAllPostsSortedByPublishDate() {
        return postRepository.findAllByOrderByPublishedAtDesc();
    }

    public List<Post> getAllPostsSortedByLastUpdate() {
        return postRepository.findAllByOrderByUpdateDateTimeDesc();
    }


    private Pageable getPageable(String orderBy, String direction, Integer pageNo, Integer pageSize) {
        System.out.println("orderBy , direction, pageNo, pageSize = " + orderBy+ " " +direction +" " + pageNo +" " + pageSize);
        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return pageable;
    }

    private Page getCustomPage(Integer pageNo, Integer pageSize, List<Post> data){
        long start =  PageRequest.of(pageNo, pageSize).getOffset();
        long end = (start + PageRequest.of(pageNo, pageSize).getPageSize()) > data.size() ? data.size() : (start + PageRequest.of(pageNo, pageSize).getPageSize());
        return new PageImpl<Post>(data.subList((int) start,(int) end),PageRequest.of(pageNo,pageSize),data.size());
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

    /*
    No user exists:
    User user = new User(); user.setEmail("admin@admin.com");user.setUsername("admin");user.setPassword("admin");
    */
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
        postRepository.save(postFromDB);
    }

    public List<Post> findJsonDataByCondition(String orderBy, String direction, int page, int size) {
        Pageable pageable = getPageable(orderBy,direction,page,size);
        Page<Post> data = postRepository.findAll(pageable);
        return data.getContent();
    }

    @Override
    public List<Post> search(String keyword) {
        return null;
    }

    public Page<Post> search(String titleWord, String contentWord, String categoryWord, String orderBy,String direction,Integer pageNo, Integer pageSize) {
        Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
        return postRepository.findDistinctByTitleContainsOrContentContainsOrCategories_categoryNameLike(titleWord, contentWord, categoryWord,pageable);
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
        return getCustomPage(pageNo,size,listCategory);
    }

    @Override
    public Page<Post> findAllByOrderBy(String orderBy, String direction, Integer page, Integer size) {
        Pageable pageable = getPageable(orderBy,direction,page,size);
        Page<Post> data = postRepository.findAll(pageable);
        return data;
    }

    public Page<Post> filterPostsMethod(String username, String tagName, String orderBy, String direction, String operation, String searchQuery, String page, String size){
        Integer pageNo = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(size);
        Page data = null;
        if(!(searchQuery.equals(""))){
            //Search with username and multiple categories: request params contain : search, username and category
            if(tagName.contains(",") && !(username.toLowerCase().equals("nouser"))){
                Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
                String[] categories = tagName.split(",");
                data = getPostsByUsernameAndSearchAndMultipleCategoriesAndOperation(username,tagName,searchQuery,orderBy,direction,pageNo,pageSize);
            }
            //Search with username and category: request params contain : search, username and category
           else if( (!(username.toLowerCase().equals("nouser"))) && tagName!= null && !(tagName.toLowerCase().equals("notag"))){
                Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
                  data = postRepository.findDistinctByUser_usernameAndCategories_categoryNameAndTitleContainingOrUser_usernameAndCategories_categoryNameAndContentContainingOrUser_usernameAndCategories_categoryNameAndCategories_categoryName(username,tagName,searchQuery,username, tagName, searchQuery,username,tagName, searchQuery, pageable);
            }

            //Search with username, request params contain : search and username
            else if( (!(username.toLowerCase().equals("nouser")))){
                Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
                data = postRepository.findDistinctByUser_usernameAndTitleContainingOrUser_usernameAndContentContainingOrUser_usernameAndCategories_categoryNameContains(username,searchQuery,username,searchQuery,username,searchQuery,pageable);
            }
            else if(tagName.contains(",")){
                String[] categories = tagName.split(",");
                Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
                data = postRepository.findDistinctByCategories_categoryNameInAndTitleContainsOrCategories_categoryNameInAndContentContains(categories,searchQuery,categories,searchQuery,pageable);
            }

            else if(tagName!= null && !(tagName.toLowerCase().equals("notag"))){
                Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
                data = postRepository.findDistinctByCategories_categoryNameLikeAndTitleContainsOrCategories_categoryNameLikeAndContentContains(tagName,searchQuery,tagName,searchQuery,pageable);
            }
            else {
                data = search(searchQuery, searchQuery, searchQuery, orderBy, direction, pageNo, pageSize);
            }
        }
        //Username with multiple categories.
        else if(tagName.contains(",") && (!(username.toLowerCase().equals("nouser")))) {
            if(operation.toLowerCase().equals("or")) {
                data = getPostsWithUsernameAndMultipleTagsOrOperation(username, tagName, orderBy, direction, pageNo, pageSize);
            }
            else{
                data = getPostsWithUsernameAndMultipleTagsAndOperation(username, tagName, orderBy, direction, pageNo, pageSize);
            }
        }

        //Username with single category.
        else if(tagName!= null && !(tagName.toLowerCase().equals("notag")) && !(username.equals("noUser"))){
            System.out.println("USER WITH SINGLE CATEGORY");
            data = getPostsWithUsernameAndTag(username,tagName,orderBy,direction,pageNo,pageSize);
        }
        else if(tagName.contains(",")){
            String [] categories = tagName.split(",");
            if(operation.toLowerCase().equals("and")) {
                data = getPostsByMultipleTags(categories, orderBy, direction, pageNo, pageSize);
            }
            else if(operation.toLowerCase().equals("or")){
//                Pageable pageable = PageRequest.of(pageNo, pageSize);
                Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
                data = postRepository.findDistinctByCategories_categoryNameIn(categories, pageable);
            }
        }
        else if(!(username.equals("noUser"))) {
            data =  getBlogPostsByUser(username, orderBy, direction, page, size);
        }
        else if( tagName!= null && !(tagName.toLowerCase().equals("notag"))){
            data  = findDataByTagNameOrderBy(tagName, orderBy, direction, pageNo, pageSize);
        }else{
            Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
            data = postRepository.findAllByOrderByIdAsc(pageable);
        }
        return data;
    }

    private Page<Post> getPostsWithUsernameAndTag(String username, String tagName, String orderBy, String direction, Integer pageNo, Integer pageSize){
        Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
        return postRepository.findAllByUser_usernameAndCategories_categoryNameContains(username,tagName,pageable);
    }

    private Page<Post> getPostsWithUsernameAndMultipleTagsOrOperation(String username, String tagName, String orderBy, String direction, Integer pageNo, Integer pageSize){
        String[] categories = tagName.split(",");
        Pageable pageable = getPageable(orderBy,direction,pageNo,pageSize);
        return postRepository.findByUser_usernameAndCategories_categoryNameIn(username,categories,pageable);
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
            if(post.getCategories().containsAll(categoryList) && post.getUser().getUsername().toLowerCase().equals(username)){
                allPostsWithAllCategories.add(post);
            }
        }

        return getCustomPage(pageNo,pageSize,allPostsWithAllCategories);
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
        return getCustomPage(pageNo,pageSize,userPosts);
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

        return getCustomPage(pageNo,pageSize,allPostsWithAllCategories);
    }

    private Page getPostsByUsernameAndSearchAndMultipleCategoriesAndOperation(String username, String tagName, String searchQuery, String orderBy, String direction, Integer pageNo, Integer pageSize){

        String[] categories = tagName.split(",");

        List<Post> allPosts = postRepository.findAll();
        List<Category> categoryList = new ArrayList<>();
        for(String categoryName: categories) {
            Category category = categoryRepository.findByCategoryName(categoryName);
            categoryList.add(category);
        }

        List<Post> allPostsWithAllCategories = new ArrayList<>();
        for(Post post: allPosts){
            if(post.getCategories().containsAll(categoryList) && post.getUser().getUsername().toLowerCase().equals(username) &&( post.getTitle().contains(searchQuery) || post.getContent().contains(searchQuery))){
                System.out.println("MATCH FOUND");
                allPostsWithAllCategories.add(post);
            }
        }
        return getCustomPage(pageNo,pageSize,allPostsWithAllCategories);

    }

}
