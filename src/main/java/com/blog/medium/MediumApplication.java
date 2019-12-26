package com.blog.medium;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MediumApplication{

    @Autowired
    static PostRepository postRepository;

    public static void main(String[] args) {

        SpringApplication.run(MediumApplication.class, args);
    }


    public static void doad() {
        Post post = new Post();
        post.setContent("Program post content");
        post.setTitle("Program post title");

        Category category = new Category();
        category.setCategoryName("spring-program");

        Category category1 = new Category();
        category1.setCategoryName("spring-program-2");

        post.getCategories().add(category);
        post.getCategories().add(category1);

        category.getPosts().add(post);
        category.getPosts().add(post);

        postRepository.save(post);


    }


}
