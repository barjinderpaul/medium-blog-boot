package com.blog.medium.controllers;

import com.blog.medium.model.Post;
import com.blog.medium.service.FilterService;
import com.blog.medium.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Controller
public class FilterController {

    @Autowired
    FilterService filterService;

    @Autowired
    PostService postService;

    @RequestMapping(value = "/posts/search", method = RequestMethod.GET)
    public ModelAndView getSearchResults(@RequestParam("query") String queryWord) {

        Set<Post> searchResults = filterService.search(queryWord,queryWord);

        Set<Post> searchResults2 = filterService.getFromCategories(queryWord);
        searchResults.addAll(searchResults2);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blogPosts");
        modelAndView.addObject("allPosts", searchResults);

        return modelAndView;
    }


    /*
     * http://localhost:8080/posts/filter?orderBy=UpdateDateTime&direction=DESC&page=1&size=2
     * http://localhost:8080/posts/filter?orderBy=PublishedAt&direction=DESC&page=1&size=2
     * */
    @RequestMapping(value = "/posts" , params = {"tag"},method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView filterPostsByUpdationDate( @RequestParam(value = "tag", required = false, defaultValue = "noTag") String tagName,
                                    @RequestParam(value = "orderBy", required = false, defaultValue = "UpdateDateTime") String orderBy,
                                    @RequestParam(value = "direction",required = false, defaultValue = "DESC") String direction,
                                    @RequestParam(value = "page",required = false, defaultValue = "0") String page,
                                    @RequestParam(value = "size",required = false ,defaultValue = "2") String size) {
        return filterService.filterPostsMethod(tagName, orderBy, direction, page, size);

    }

    @RequestMapping(value = "/posts" , params = {"tag","orderBy=CreateDateTime"},method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView filterPostsByCreationDate( @RequestParam(value = "tag", required = false, defaultValue = "noTag") String tagName,
                                     @RequestParam(value = "orderBy", required = false, defaultValue = "CreateDateTime") String orderBy,
                                     @RequestParam(value = "direction",required = false, defaultValue = "DESC") String direction,
                                     @RequestParam(value = "page",required = false, defaultValue = "0") String page,
                                     @RequestParam(value = "size",required = false ,defaultValue = "2") String size) {
        return filterService.filterPostsMethod(tagName, orderBy, direction, page, size);
    }

    @RequestMapping(value = "/posts",params = {},method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView filterPosts( @RequestParam(value = "tag", required = false, defaultValue = "noTag") String tagName,
                                                   @RequestParam(value = "orderBy", required = false, defaultValue = "CreateDateTime") String orderBy,
                                                   @RequestParam(value = "direction",required = false, defaultValue = "DESC") String direction,
                                                   @RequestParam(value = "page",required = false, defaultValue = "0") String page,
                                                   @RequestParam(value = "size",required = false ,defaultValue = "2") String size) {
        System.out.println("IN FILTER POSTS");
        return filterService.filterPostsMethod(tagName, orderBy, direction, page, size);
    }

    @RequestMapping(value = "/posts" , params = {"user"},method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getBlogPostsByUser( @RequestParam(value = "user", required = true, defaultValue = "admin") String userName,
                                                   @RequestParam(value = "orderBy", required = false, defaultValue = "UpdateDateTime") String orderBy,
                                                   @RequestParam(value = "direction",required = false, defaultValue = "DESC") String direction,
                                                   @RequestParam(value = "page",required = false, defaultValue = "0") String page,
                                                   @RequestParam(value = "size",required = false ,defaultValue = "2") String size) {

        Page<Post> posts =  filterService.getBlogPostsByUser(userName, orderBy, direction, page, size);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("filteredPosts");
        modelAndView.addObject("posts",posts.getContent());
        modelAndView.addObject("postsPage",posts);
        modelAndView.addObject("numbers", IntStream.range(0,posts.getTotalPages()).toArray());
        return modelAndView;


    }

}
