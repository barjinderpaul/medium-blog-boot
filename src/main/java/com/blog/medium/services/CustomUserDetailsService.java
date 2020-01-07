package com.blog.medium.services;

import com.blog.medium.model.User;
import com.blog.medium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        CustomUserDetails customUserDetails = null;
        if(user == null){
            throw new UsernameNotFoundException("No user found with username: " + user);
        }
        else {
            customUserDetails = new CustomUserDetails();
            customUserDetails.setUser(user);
        }
        return customUserDetails;

    }
}
