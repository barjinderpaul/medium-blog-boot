package com.blog.medium.controllers;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import com.blog.medium.service.CategoryService;
import com.blog.medium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/posts/tag/{categoryName}/{categoryId}"}, method= RequestMethod.GET)
    public ModelAndView getBlogPosts(@PathVariable("categoryName") String categoryName, @PathVariable("categoryId") String categoryId){

        Long category_id = Long.parseLong(categoryId);
        List<Category> categories = categoryService.getAllTags();
        List<User> users = userService.getAllUsers();


        Category category = categoryService.getCategory(category_id);
        Set<Post> posts = category.getPosts();

        System.out.println("SIZE ++++++ " + posts.size());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blogPosts");
        modelAndView.addObject("allPosts",posts);
        modelAndView.addObject("allCategories",categories);
        modelAndView.addObject("allUsers",users);

        return modelAndView;
    }


}
