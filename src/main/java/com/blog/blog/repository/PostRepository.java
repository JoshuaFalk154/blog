package com.blog.blog.repository;

import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findByAuthor(User user);
}
