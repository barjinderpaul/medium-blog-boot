package com.blog.medium.controllers;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import com.blog.medium.service.CategoryService;
import com.blog.medium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView getBlogPosts( @PathVariable("categoryId") String categoryId){

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


    @RequestMapping(value = {"/posts/tag/{categoryName}/{categoryId}/filter"}, params = {"orderBy", "direction", "page", "size"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView filterPosts(@PathVariable("categoryName") String categoryName,@PathVariable("categoryId") String category_id, @RequestParam("orderBy") String orderBy, @RequestParam("direction") String direction, @RequestParam("page") String page, @RequestParam("size") String size) {

        Integer pageNo = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(size);
        Long categoryId = Long.parseLong(category_id);
        List<Post> list = categoryService.findJsonDataByCondition(categoryId,categoryName,orderBy, direction, pageNo, pageSize);
        for (Post post : list) {
            System.out.println("POST++++++ " + post.getId() + " " + post.getTitle() + " " + post.getContent() + " ");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blogPosts");
        modelAndView.addObject("allPosts",list);

        return modelAndView;
    }

}
