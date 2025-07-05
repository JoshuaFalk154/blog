package com.blog.blog.repository;

import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByAuthor(User user);
}
