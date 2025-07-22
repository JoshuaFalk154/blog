package com.blog.blog.service;

import com.blog.blog.entities.Like;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.LikeAlreadyExistsException;
import com.blog.blog.exceptions.LikeNotFoundException;
import com.blog.blog.exceptions.UserNotExistingException;
import com.blog.blog.repository.LikeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

    @Mock
    UserService userService;

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
    void LikeService_addLike_UserNotExistingException() {
        User user = User.builder()
                .sub("somesub")
                .email("some@mail.com")
                .id(UUID.randomUUID())
                .build();

        UUID postId = UUID.randomUUID();

        when(likeRepository.existsByUserAndPostId(user, postId)).thenReturn(false);
        when(userService.userExists(user.getSub())).thenReturn(false);

        assertThrows(UserNotExistingException.class, () -> likeService.addLike(postId, user));
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

    @Test
    void LikeService_deleteLike_UserNotExistingException() {
        User user = User.builder()
                .sub("somesub")
                .email("some@mail.com")
                .id(UUID.randomUUID())
                .build();
        UUID postId = UUID.randomUUID();

        Post post = Post.builder()
                .id(postId)
                .author(user)
                .title("title")
                .body("body")
                .build();

        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();

        when(likeRepository.findByUserAndPostId(user, postId)).thenReturn(Optional.of(like));
        when(userService.userExists(user.getSub())).thenReturn(false);

        assertThrows(UserNotExistingException.class, () -> likeService.deleteLike(postId, user));
    }

}
