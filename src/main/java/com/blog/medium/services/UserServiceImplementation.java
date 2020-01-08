package com.blog.medium.services;

import com.blog.medium.exceptions.InvalidArgumentException;
import com.blog.medium.exceptions.NotFoundException;
import com.blog.medium.model.ConfirmationToken;
import com.blog.medium.model.Role;
import com.blog.medium.model.User;
import com.blog.medium.repository.ConfirmationTokenRepository;
import com.blog.medium.repository.RoleRepository;
import com.blog.medium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String getErrorOrLogoutMessage(String error, String message) {
        if (error != null) {
            return "Invalid login/password";
        }
        return "Logout success!";
    }

    @Override
    @Transactional
    public void registerUer(String username, String password, String email) {

        checkValidRegistration(username,password,email);

        User isAlreadyExistingUser = userRepository.findByUsername(username);

        User user = null;
        if(isAlreadyExistingUser == null) {
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));

            Role userRole = roleRepository.findByRoleName("USER");
            if (userRole == null) {
                Role userR = new Role();
                userR.setRoleName("USER");
                roleRepository.save(userR);
                userRole = userR;
            }
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            user.setRoles(userRoles);
            userRepository.save(user);
        }
        else { user = isAlreadyExistingUser; }

        ConfirmationToken existingConfirmationToken = confirmationTokenRepository.findByUser_Username(user.getUsername());
        if (existingConfirmationToken != null ){
            confirmationTokenRepository.deleteConfirmationTokenByUser_Id(existingConfirmationToken.getUser().getId());
        }

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("ibennysingh@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"https://cedium.herokuapp.com/confirm-account?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

    }

    @Override
    public ModelAndView confirmAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        ModelAndView modelAndView = new ModelAndView();
        if(token != null) {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setIsEnabled(true);
            userRepository.save(user);
            modelAndView.setViewName("accountVerified");
        }
        else {
            modelAndView.addObject("message","The link is invalid or the token has expired!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @Override
    @Transactional
    public String resetPassword(String username) {
        User user = userRepository.findByUsername(username);
        String message = "";
//        ModelAndView modelAndView = new ModelAndView();
        if(user == null){
            return "Username does not exists";
        }

        ConfirmationToken isExistingConfirmationToken = confirmationTokenRepository.findByUser_Username(user.getUsername());
        if(isExistingConfirmationToken != null) {
            confirmationTokenRepository.deleteConfirmationTokenByTokenid(isExistingConfirmationToken.getTokenid());
        }

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Forgot Password");
        mailMessage.setFrom("ibennysingh@gmail.com");
        mailMessage.setText("To reset your account password, please click here : "
                +"http://ec2-13-233-120-185.ap-south-1.compute.amazonaws.com:8080/blog/forget-account-password?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

//        modelAndView.addObject("message", "Password reset link sent on " +user.getEmail() + " .");
        message = "Password reset link sent on : " + user.getEmail() + "";

        return message;
    }

    @Override
    public String isValidToken(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null) {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            return user.getUsername();
        }
        return "The link is invalid or the token has expired!";
    }

    @Override
    public String registerAdmin(String username, String password, String email, String key) {
        checkValidRegistration(username,password,email);
        if( !(key.equals("PRIVATE_KEY")) ) {
            return "key is not valid";
        }
        User isAlreadyExistingUser = userRepository.findByUsername(username);

        User user = null;
        if(isAlreadyExistingUser == null) {
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));

            Role adminRole = roleRepository.findByRoleName("ADMIN");
            if (adminRole == null) {
                Role userR = new Role();
                userR.setRoleName("ADMIN");
                roleRepository.save(userR);
                adminRole = userR;
            }
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(adminRole);
            user.setRoles(userRoles);
            userRepository.save(user);
        }
        else { user = isAlreadyExistingUser; }

        ConfirmationToken existingConfirmationToken = confirmationTokenRepository.findByUser_Username(user.getUsername());
        if (existingConfirmationToken != null ){
            confirmationTokenRepository.deleteConfirmationTokenByUser_Id(existingConfirmationToken.getUser().getId());
        }

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("ibennysingh@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://ec2-13-233-120-185.ap-south-1.compute.amazonaws.com:8080/blog/confirm-account?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);
        return "valid";
    }




    private void checkValidRegistration(String username, String password, String email) {
        if(username==null || username.equals("") ){
            throw new InvalidArgumentException("Username: " + username + " is invalid");
        }
        if(email==null || email.equals("")){
            throw new InvalidArgumentException("Email: " + email + " is invalid");
        }

        System.out.println("USERNAME = " + username);
        User existingUserWithUsername = userRepository.findByUsername(username);
        System.out.println("ALREADY EXISTING? : " + existingUserWithUsername == null);
        if((existingUserWithUsername != null && existingUserWithUsername.getIsEnabled() && (email.equals(existingUserWithUsername.getEmail())))){
            throw new InvalidArgumentException("Username: " + username + " already exists");
        }

        if(existingUserWithUsername != null && !existingUserWithUsername.getEmail().equals(email)){
            System.out.println("existing user's email , current email , equal?");
            throw new InvalidArgumentException("Username: " + username + " already exists");
        }

        User existingUserWithEmail = userRepository.findByEmailIgnoreCase(email);

        if((existingUserWithEmail != null && !existingUserWithEmail.getUsername().equals(username))){
            throw new InvalidArgumentException("Email: " + email + " already exists");
        }
    }
}
