package com.blog.blog.repository;

import com.blog.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findBySub(String sub);
    boolean existsUserBySubNotAndEmail(String sub, String email);
    boolean existsUserByEmail(String email);
    boolean existsUserBySub(String sub);

}
