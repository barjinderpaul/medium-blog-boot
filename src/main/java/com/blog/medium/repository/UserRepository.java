package com.blog.medium.repository;

import com.blog.medium.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    User findByEmailIgnoreCase(String email);
//    Page findPostsByUsernameAndPosts_Categories_categoryNameContains(String username, String category, Pageable pageable);
    void deleteUserByUsername(String username);
}
