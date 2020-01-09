package com.blog.medium.services;

public interface UserService {

    void registerUer(String username, String password, String email);

    String confirmAccount(String confirmationToken);

    String resetPassword(String username);

    String isValidToken(String confirmationToken);

    String registerAdmin(String username, String password, String email, String key);

    String getErrorOrLogoutMessage(String error, String message);
}
