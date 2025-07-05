package com.blog.blog.repository;

import com.blog.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySub(String sub);
    boolean existsUserBySubNotAndUsername(String sub, String username);
    boolean existsUserByUsername(String username);

}
