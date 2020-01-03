package com.blog.medium.controllers;

import com.blog.medium.model.ConfirmationToken;
import com.blog.medium.model.Role;
import com.blog.medium.model.User;
import com.blog.medium.repository.ConfirmationTokenRepository;
import com.blog.medium.repository.RoleRepository;
import com.blog.medium.repository.UserRepository;
import com.blog.medium.service.EmailSenderService;
import com.blog.medium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RestController
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/login")
    public ModelAndView lig(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }


    @PostMapping("/register")
    public ModelAndView registerSuccessful(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) {

        userService.registerUer(username, password, email);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("emailId", email);
        modelAndView.setViewName("successfulRegisteration");

       return modelAndView;

    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        return userService.confirmAccount(confirmationToken);
    }


/*
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/admin/add")
    public String addUserAsAdmin(*/
/*@RequestParam("name") String username, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("roles") String[] roles*//*
 @RequestBody User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "User added successfully";
    }
*/

    @GetMapping("/forgot-password")
    public ModelAndView forgotPassword(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }

    @PostMapping("/forgot-password")
    public ModelAndView resetPassword(@RequestParam("username") String username){
        return userService.resetPassword(username);
    }

    @GetMapping("/forget-account-password")
    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam("token")String confirmationToken){
       return userService.setNewPassword(confirmationToken);
    }

    @PostMapping("/forget-account-password")
    public ModelAndView setPassword(@RequestParam("username")String username, @RequestParam("password") String password){
        User user = userRepository.findByUsername(username);
        ModelAndView modelAndView = new ModelAndView();
        if(user == null ){
            modelAndView.addObject("message","Invalid User!");
            modelAndView.setViewName("error");
        }
        else{
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
            modelAndView.setViewName("messagePage");
            modelAndView.addObject("message","Password chnaged successfully");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

}
