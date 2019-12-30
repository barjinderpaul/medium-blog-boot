package com.blog.medium.controllers;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.repository.PostRepository;
import com.blog.medium.service.CategoryService;
import com.blog.medium.service.PostService;
import com.blog.medium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@org.springframework.web.bind.annotation.RestController
public class ApiRestController {
    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/api/posts/{id}",method = RequestMethod.GET, produces = {"application/json"})
    public Post getPost(@PathVariable("id") Long id) {
        Post post = postService.getPost(id);
        return post;
    }

    @RequestMapping(value = "/api/posts" ,method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public List<Post> filterPostsByCreationDate(@RequestParam(value = "operation",required = false,defaultValue = "and") String operation,
                                                  @RequestParam(value = "user",required = false,defaultValue = "noUser") String username,
                                                  @RequestParam(value = "search",required = false,defaultValue = "") String searchQuery,
                                                  @RequestParam(value = "tag", required = false, defaultValue = "noTag") String tagName,
                                                  @RequestParam(value = "orderBy", required = false, defaultValue = "CreateDateTime") String orderBy,
                                                  @RequestParam(value = "direction",required = false, defaultValue = "DESC") String direction,
                                                  @RequestParam(value = "page",required = false, defaultValue = "0") String page,
                                                  @RequestParam(value = "size",required = false ,defaultValue = "10") String size) {

        Page<Post> data = null;
        if(searchQuery.equals("")){
            data = postService.filterPostsMethodWithoutSearch(username, tagName, orderBy, direction,operation,page, size);
        }
        else {
            data = postService.filterPostsMethodBySearch(username, tagName, orderBy, direction, operation, searchQuery, page, size);
        }

        return data.getContent();
    }

    @RequestMapping(value = "/api/posts",method = RequestMethod.POST)
    public Post addPost(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "categories" , required = false) String[] categories) {

        List<String> categoriesList;
        if(categories != null) {
            categoriesList = Arrays.asList(categories);
        }
        else{
            categoriesList = new ArrayList<>();
            categoriesList.add("uncategorized");
        }
        Long post_id = postService.addPost(title,content,categoriesList);
        Post post = postService.getPost(post_id);
        return post;

    }

    @RequestMapping(value = "/api/posts/delete/{id}",method = RequestMethod.DELETE)
    public void deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
    }

    @RequestMapping(value = "/api/posts/update/{id}",method = RequestMethod.PUT)
    public Post updatePost(@PathVariable("id") Long id, @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "categories", required = false) String[] categories) {

        List<String> categoriesList;
        if(categories != null) {
            categoriesList = Arrays.asList(categories);
        }
        else{
            categoriesList = new ArrayList<>();
        }

        Long post_id = postService.updatePost(id,title,content,categoriesList);
        Post post = postService.getPost(post_id);
        return post;
    }



}
