package com.blog.medium.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Qualifier("customUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
         .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/").permitAll()
                .antMatchers("/confirm-account").permitAll()
                .antMatchers("/forgot-password").permitAll()
                .antMatchers("/forget-account-password").permitAll()
                .antMatchers("/posts/{id}").permitAll()
                .antMatchers(HttpMethod.GET,"/posts/").permitAll()
                .antMatchers(HttpMethod.GET,"/login").permitAll()
                .antMatchers(HttpMethod.GET,"/register").permitAll()
                .antMatchers(HttpMethod.POST,"/register").permitAll()
                .antMatchers(HttpMethod.GET,"/register/admin").permitAll()
                .antMatchers(HttpMethod.POST,"/register/admin").permitAll()
                .antMatchers(HttpMethod.GET,"/error").permitAll()
                .antMatchers(HttpMethod.GET,"/posts/add/").authenticated()
                .antMatchers(HttpMethod.POST,"/posts/add/").authenticated()
                .antMatchers(HttpMethod.POST,"/posts/").authenticated()
                .antMatchers(HttpMethod.PUT,"/posts/").authenticated()
                .antMatchers(HttpMethod.PATCH,"/posts/").authenticated()
                .antMatchers(HttpMethod.DELETE,"/posts/").authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/posts")
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll();
    }
}
