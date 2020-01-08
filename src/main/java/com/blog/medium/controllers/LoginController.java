package com.blog.medium.controllers;

import com.blog.medium.model.User;
import com.blog.medium.repository.RoleRepository;
import com.blog.medium.repository.UserRepository;
import com.blog.medium.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout",required = false) String logout){
        log.info("Login started");
        String message = null;
        if (error != null || logout != null) {
            message = userService.getErrorOrLogoutMessage(error,logout);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message",message);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/register/admin")
    public ModelAndView registerAdmin(){
        log.info("GET: /register/admin");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminRegister");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register(){
        log.info("GET: /register");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/register/admin")
    public ModelAndView registerSuccessfulAdmin(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email, @RequestParam("key")String key) {
        log.info("POST: /register, Starting registration of user");

        userService.registerAdmin(username, password, email,key);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("emailId", email);
        modelAndView.setViewName("successfulRegisteration");

        return modelAndView;

    }


    @PostMapping("/register")
    public ModelAndView registerSuccessful(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) {
        log.info("POST: /register, Starting registration of user");

        userService.registerUer(username, password, email);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("emailId", email);
        modelAndView.setViewName("successfulRegisteration");

       return modelAndView;

    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        log.info("GET: /confirm-account, confirming user account");
        return userService.confirmAccount(confirmationToken);
    }

    @GetMapping("/forgot-password")
    public ModelAndView forgotPassword(){
        log.info("GET: /forgot-password, page for forget password");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }

    @PostMapping("/forgot-password")
    public ModelAndView resetPassword(@RequestParam("username") String username){
        log.info("POST: /forgot-password, process for resetting password started");
        String message =  userService.resetPassword(username);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message",message);
        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }

    @GetMapping("/forget-account-password")
    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam("token")String confirmationToken){
       log.info("GET: /forget-account-password");
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
            modelAndView.addObject("message","Password changed successfully");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

}
