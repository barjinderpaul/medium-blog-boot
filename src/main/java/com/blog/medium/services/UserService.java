package com.blog.medium.services;

import com.blog.medium.model.User;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface UserService {

    void registerUer(String username, String password, String email);

    ModelAndView confirmAccount(String confirmationToken);

    ModelAndView resetPassword(String username);

    ModelAndView setNewPassword(String confirmationToken);

    void registerAdmin(String username, String password, String email, String key);

    String getErrorOrLogoutMessage(String error, String message);
}
