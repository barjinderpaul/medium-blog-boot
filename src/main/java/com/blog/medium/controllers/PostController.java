package com.blog.medium.controllers;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import com.blog.medium.service.CategoryService;
import com.blog.medium.service.PostService;
import com.blog.medium.model.Post;
import com.blog.medium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/","/posts"}, method= RequestMethod.GET)
    public ModelAndView getBlogPosts(){

       List<Post> allPosts = postService.getAllPosts();
       List<Category> categories = categoryService.getAllTags();
        System.out.println("SIZE ++++++ " + categories.size());

        List<User> users = userService.getAllUsers();

       ModelAndView modelAndView = new ModelAndView();
       modelAndView.setViewName("blogPosts");
       modelAndView.addObject("allCategories",categories);
       modelAndView.addObject("allPosts",allPosts);
       modelAndView.addObject("allUsers",users);

       return modelAndView;
    }

    @RequestMapping(value = {"/posts/sort/publish"}, method= RequestMethod.GET)
    public ModelAndView getBlogPostsSortedByPublishDate(){

        List<Post> allPosts = postService.getAllPostsSortedByPublishDate();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blogPosts");
        modelAndView.addObject("allPosts",allPosts);

        return modelAndView;
    }

    @RequestMapping(value = {"/posts/sort/update"}, method= RequestMethod.GET)
    public ModelAndView getBlogPostsSortedByUpdationDate(){

        List<Post> allPosts = postService.getAllPostsSortedByLastUpdate();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blogPosts");
        modelAndView.addObject("allPosts",allPosts);

        return modelAndView;
    }




    @RequestMapping(value = "posts/add",method = RequestMethod.GET)
    public ModelAndView redirectToCreatePost(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("createPost");
        modelAndView.addObject("heading","Add Post");
        modelAndView.addObject("customAction","addPost");

        return modelAndView;
    }



    @RequestMapping(value = "posts/add",method = RequestMethod.POST)
    public ModelAndView addPost(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("categories") String[] categories) {

        List<String> categoriesList =Arrays.asList(categories);
        Long post_id = postService.addPost(title,content,categoriesList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("createPost");
        modelAndView.addObject("postCreated","true");
        modelAndView.addObject("title",title);
        modelAndView.addObject("heading","Post created");
        modelAndView.addObject("id",post_id);
        modelAndView.addObject("customAction","postCreated");

        return modelAndView;
    }



    @RequestMapping(value = "posts/{id}",method = RequestMethod.GET)
    public ModelAndView getPost(@PathVariable("id") Long id) {

       Post post = postService.getPost(id);

       ModelAndView modelAndView = new ModelAndView();
       modelAndView.setViewName("singlePost");
       modelAndView.addObject("post",post);

       return modelAndView;
    }


    @RequestMapping(value = "posts/delete/{id}", method = RequestMethod.GET)
    public ModelAndView redirectToDeletePage(@PathVariable("id") String id){
        Long postId = Long.parseLong(id);

        Post post = postService.getPost(postId);

        String content = post.getContent();
        String title = post.getTitle();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("deletePost");
        modelAndView.addObject("customAction","addPost");
        modelAndView.addObject("id",postId);
        modelAndView.addObject("content",content);
        modelAndView.addObject("title",title);

        return modelAndView;
    }

    @RequestMapping(value = "posts/delete/{id}",method = RequestMethod.POST)
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/";

    }

    @RequestMapping(value = "posts/update/{id}" ,method = RequestMethod.GET)
    public ModelAndView redirectToUpdatePost( @PathVariable("id") String id) {
        Long postId = Long.parseLong(id);

        Post post = postService.getPost(postId);
        String content = post.getContent();
        String title = post.getTitle();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("createPost");
        modelAndView.addObject("heading","Update Post");
        modelAndView.addObject("customAction","updatePost");
        modelAndView.addObject("content","some-sample-content");
        modelAndView.addObject("id",postId);
        modelAndView.addObject("content",content);
        modelAndView.addObject("title",title);

        return modelAndView;
    }

    @RequestMapping(value = "posts/update/{id}",method = RequestMethod.POST)
    public String updatePost(@PathVariable("id") Long id, @RequestParam("title") String title, @RequestParam("content") String content) {

        postService.updatePost(id,title,content);
        return "redirect:/posts/{id}";
    }

}
