package com.blog.blog.service;

import com.blog.blog.dto.PostCreate;
import com.blog.blog.dto.PostCreated;
import com.blog.blog.dto.PostFull;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.PostNotFoundException;
import com.blog.blog.exceptions.UserNotExistingException;
import com.blog.blog.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceUT {


    @Mock
    private EntityManager entityManager;

    @Mock
    private Validator validator;

    @Mock
    private UserService userService;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void PostService_addPost_AddedPost() {
        User userInput = User.builder()
                .id(UUID.randomUUID())
                .sub(UUID.randomUUID().toString())
                .build();

        PostCreate postCreate = new PostCreate("some title", "some body");

        when(validator.validate(postCreate)).thenReturn(new HashSet<>());
        when(userService.userExists(userInput.getSub())).thenReturn(true);
        when(entityManager.merge(userInput)).thenReturn(userInput);

        Post postResult = postService.addPost(userInput, postCreate);

        assertThat(postResult).isNotNull();
    }


    @ParameterizedTest
    @MethodSource("provideInvalidPostCreates")
    public void PostService_addPost_IllegalInput_IllegalArgumentException() {
        User userInput = User.builder()
                .id(UUID.randomUUID())
                .sub(UUID.randomUUID().toString())
                .build();

        PostCreate postCreate = new PostCreate("some title", "some body");

        ConstraintViolation<PostCreate> mockViolation = mock(ConstraintViolation.class);
        when(validator.validate(postCreate)).thenReturn(new HashSet<>(Set.of(mockViolation)));

        assertThrows(IllegalArgumentException.class, () -> postService.addPost(userInput, postCreate));
    }

    private static Stream<Arguments> provideInvalidPostCreates() {
        return Stream.of(
          Arguments.of(new PostCreate("", "Some body")),
                Arguments.of(new PostCreate("Some Title", ""))
        );
    }

    @Test
    public void PostService_addPost_UserNotExisting_UserNotExistingException() {
        User userInput = User.builder()
                .id(UUID.randomUUID())
                .sub(UUID.randomUUID().toString())
                .build();

        PostCreate postCreate = new PostCreate("some title", "some body");
        when(validator.validate(postCreate)).thenReturn(new HashSet<>());
        when(userService.userExists(userInput.getSub())).thenReturn(false);

        assertThrows(UserNotExistingException.class, () -> postService.addPost(userInput, postCreate));
    }

    @Test
    public void PostService_getPost_AllGood_ReturnPostFull() {
        UUID postId = UUID.randomUUID();
        PostFull expected = new PostFull(
                "sometitle",
                "some body",
                postId,
                "author@mail.com",
                Date.from(Instant.now()),
                Date.from(Instant.now()),
                100L
        );

        when(postRepository.findPostWithNumOfLikes(postId)).thenReturn(Optional.of(expected));

        PostFull result = postService.getPost(postId);

        assertEquals(expected, result);
    }

    @Test
    public void PostService_getPost_PostNotExisting_PostNotFoundException() {
        UUID postId = UUID.randomUUID();
        when(postRepository.findPostWithNumOfLikes(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPost(postId));
    }
}
