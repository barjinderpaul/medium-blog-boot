package com.blog.medium.service;

import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface UserService {
//    List<Post> getUserPosts(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
    User save(User user);
    void registerUer(String username, String password, String email);

    ModelAndView confirmAccount(String confirmationToken);

    ModelAndView resetPassword(String username);

    ModelAndView setNewPassword(String confirmationToken);

    void registerAdmin(String username, String password, String email, String key);
}
