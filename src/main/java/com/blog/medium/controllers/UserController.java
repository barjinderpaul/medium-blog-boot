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
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/posts/user/{username}/{userId}"}, method= RequestMethod.GET)
    public ModelAndView getBlogPosts(@PathVariable("username") String username, @PathVariable("userId") String userId){

        Long user_id = Long.parseLong(userId);

        User user = userService.getUserById(user_id);
        List<Post> userPosts = user.getPosts();
        System.out.println("SIZE ++++++ " + userPosts.size());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blogPosts");
        modelAndView.addObject("allPosts",userPosts);

        return modelAndView;
    }

}
