package com.blog.medium.repository;

import com.blog.medium.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    void deleteConfirmationTokenByTokenid(Long tokenId);
}
