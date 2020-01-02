package com.blog.medium.controllers;

import com.blog.medium.model.ConfirmationToken;
import com.blog.medium.model.Role;
import com.blog.medium.model.User;
import com.blog.medium.repository.ConfirmationTokenRepository;
import com.blog.medium.repository.RoleRepository;
import com.blog.medium.repository.UserRepository;
import com.blog.medium.service.EmailSenderService;
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

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

    @PostMapping("/register")
    public ModelAndView registerSuccessful(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        Role userRole = roleRepository.findByRoleName("USER");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);


        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        System.out.println("HEREREREERE + " );

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("ibennysingh@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("emailId", user.getEmail());

        modelAndView.setViewName("successfulRegisteration");


       /* ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("");*/
       return modelAndView;

    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setIsEnabled(true);
            userRepository.save(user);
            modelAndView.setViewName("accountVerified");
        }
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
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
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findByUsername(username);
        if(user==null){
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        System.out.println("HEREREREERE + " );

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Forgot Password");
        mailMessage.setFrom("ibennysingh@gmail.com");
        mailMessage.setText("To reset your account password, please click here : "
                +"https://cedium.herokuapp.com/forget-account-password?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

        modelAndView.addObject("message", "Password reset link sent on " +user.getEmail() + " .");

        modelAndView.setViewName("messagePage");
        return modelAndView;
    }

    @GetMapping("/forget-account-password")
    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam("token")String confirmationToken){
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            modelAndView.addObject("username",user.getUsername());
            modelAndView.setViewName("setNewPassword");
        }
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
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
}
