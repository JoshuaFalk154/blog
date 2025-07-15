package com.blog.blog.repository;

import com.blog.blog.entities.Like;
import com.blog.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
   boolean existsByUserAndPostId(User user, UUID postId);
   void deleteByUserAndPostId(User user, UUID postId);
   Optional<Like> findByUserAndPostId(User user, UUID postId);
}
