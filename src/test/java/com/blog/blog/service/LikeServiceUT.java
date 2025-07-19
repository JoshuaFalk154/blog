package com.blog.blog.service;

import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.LikeAlreadyExistsException;
import com.blog.blog.exceptions.LikeNotFoundException;
import com.blog.blog.repository.LikeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeServiceUT {

    @Mock
    LikeRepository likeRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    PostService postService;

    @InjectMocks
    LikeService likeService;

    @Test
    void LikeService_addLike_LikeAlreadyExistsException() {
        User user = User.builder()
                .sub("somesub")
                .email("some@mail.com")
                .id(UUID.randomUUID())
                .build();

        UUID postId = UUID.randomUUID();
        Post post = Post.builder()
                .id(postId)
                .title("title")
                .body("body")
                .build();

        when(likeRepository.existsByUserAndPostId(user, postId)).thenReturn(true);
        assertThrows(LikeAlreadyExistsException.class, () -> likeService.addLike(postId, user));
    }

    @Test
    void LikeService_deleteLike_LikeNotFoundException() {
        User user = User.builder()
                .sub("somesub")
                .email("some@mail.com")
                .id(UUID.randomUUID())
                .build();

        UUID postId = UUID.randomUUID();

        when(likeRepository.findByUserAndPostId(user, postId)).thenReturn(Optional.empty());

        assertThrows(LikeNotFoundException.class, () -> likeService.deleteLike(postId, user));
    }

}
