package com.blog.medium.repository;

import com.blog.medium.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    void deleteConfirmationTokenByTokenid(Long tokenId);
    void deleteConfirmationTokenByUser_Username(String username);
    void deleteConfirmationTokenByUser_Id(Long id);
    ConfirmationToken findByUser_Username(String username);
}
