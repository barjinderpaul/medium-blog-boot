package com.blog.medium.service;
import com.blog.medium.model.Post;
import com.blog.medium.repository.PostRepository;
import com.blog.medium.model.Post;

import com.blog.medium.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Service("PostService")
@Transactional
public class PostServiceImplementation implements PostService {

    @Autowired
    private PostRepository<Post> postRepository;

    public List<Post> getAllPosts(){
        return postRepository.findAllByOrderByIdAsc();
    }

    public Post getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.isPresent() ? post.get() :null;
    }

    public  Long addPost(String title, String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post).getId();
    }

    public  void deletePost(Long id){
        postRepository.deleteById(id);
    }

    public  void updatePost(Long id, String title, String content) {
        Post post = new Post();
        post.setContent(content);
        post.setId(id);
        post.setTitle(title);
        postRepository.save(post);
    }
}
