package com.blog.medium.controllers;

import com.blog.medium.model.Post;
import com.blog.medium.service.FilterService;
import com.blog.medium.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

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
//    params = {"tag","orderBy", "direction", "page", "size"}
    @RequestMapping(value = "/posts/filter" , method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView filterPosts( @RequestParam(value = "tag", required = false, defaultValue = "noTag") String tagName,
                                    @RequestParam(value = "orderBy", required = false, defaultValue = "UpdateDateTime") String orderBy,
                                    @RequestParam(value = "direction",required = false, defaultValue = "ASC") String direction,
                                    @RequestParam(value = "page",required = false, defaultValue = "0") String page,
                                    @RequestParam(value = "size",required = false ,defaultValue = "10") String size
                                    ) {



           Integer pageNo = Integer.parseInt(page);
           Integer pageSize =    Integer.parseInt(size);


        System.out.println("HERE pageNo and pageSize = " + pageNo  + "  " + pageSize);

        List<Post> list = null;
        if( tagName!= null && !(tagName.toLowerCase().equals("noTag"))){
             list = filterService.findDataByTagName(tagName, orderBy, direction, pageNo, pageSize);
        }
        else{
            list = postService.findJsonDataByCondition(orderBy, direction, pageNo, pageSize);
            for (Post post : list) {
                System.out.println("POST++++++ " + post.getId() + " " + post.getTitle() + " " + post.getContent() + " ");
            }
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blogPosts");
        modelAndView.addObject("allPosts",list);

        return modelAndView;
    }

}
