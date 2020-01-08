package com.blog.medium.controllers;

import com.blog.medium.exceptions.InvalidArgumentException;
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
        ModelAndView modelAndView = new ModelAndView();

        String message = userService.registerAdmin(username, password, email,key);
        if(message.equals("valid")) {
            modelAndView.addObject("emailId", email);
            modelAndView.setViewName("successfulRegisteration");
            return modelAndView;
        }

        modelAndView.addObject("message",message);
        modelAndView.setViewName("adminRegister");
        return modelAndView;
    }


    @PostMapping("/register")
    public ModelAndView registerSuccessful(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) {
        log.info("POST: /register, Starting registration of user");

        userService.registerUer(username, password, email);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "Confirmation mail sent on : " + email + ", please confirm your account and login");
        modelAndView.setViewName("login");

       return modelAndView;

    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        log.info("GET: /confirm-account, confirming user account");
        ModelAndView modelAndView1 = new ModelAndView();

        String message  = userService.confirmAccount(confirmationToken);
        modelAndView.addObject("message",message);

        if(message.contains("invalid")){
            modelAndView.setViewName("error");
        }
        else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
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
    public ModelAndView redirectToSetPassword(@RequestParam("token")String confirmationToken){
       log.info("GET: /forget-account-password");
        ModelAndView modelAndView = new ModelAndView();

        String errorMessageOrUsername =  userService.isValidToken(confirmationToken);
        if(errorMessageOrUsername.contains("invalid")){
            modelAndView.addObject("message","The link is invalid or the token has expired!");
            modelAndView.setViewName("error");
            return modelAndView;
        }

        String username = errorMessageOrUsername;
        modelAndView.addObject("username",username);
        modelAndView.addObject("confirmUsername",username);
        modelAndView.setViewName("setNewPassword");

        return modelAndView;
    }

    @PostMapping("/forget-account-password")
    public ModelAndView setPassword(@RequestParam("username")String username, @RequestParam("confirmUsername")String confirmUsername,@RequestParam("password") String password){

        User user = userRepository.findByUsername(username);
        ModelAndView modelAndView = new ModelAndView();
        if(user == null ){
            modelAndView.addObject("message","Invalid User!");
            modelAndView.setViewName("error");
        }
        else if( !(username.equals(confirmUsername))){
            throw new InvalidArgumentException("Confirmation token is not valid for username : " + username);
        }
        else{
            user.setPassword(bCryptPasswordEncoder.encode(password));
            Long userId = userRepository.save(user).getId();
            modelAndView.setViewName("login");
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
