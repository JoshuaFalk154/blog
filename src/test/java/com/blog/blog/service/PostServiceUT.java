package com.blog.blog.service;

import com.blog.blog.dto.PostCreate;
import com.blog.blog.dto.PostFull;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.InvalidPaginationException;
import com.blog.blog.exceptions.PostNotFoundException;
import com.blog.blog.exceptions.UserNotExistingException;
import com.blog.blog.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void PostService_getPostFull_AllGood_ReturnPostFullFull() {
        UUID postId = UUID.randomUUID();
        PostFull expected = new PostFull(
                "sometitle",
                "some body",
                postId,
                "author@mail.com",
                LocalDate.from(LocalDate.now()),
                LocalDate.from(LocalDate.now()),
                100L
        );

        when(postRepository.findPostWithNumOfLikes(postId)).thenReturn(Optional.of(expected));

        PostFull result = postService.getPostFull(postId);

        assertEquals(expected, result);
    }

    @Test
    public void PostService_getPostFull_PostNotExisting_PostFullNotFoundException() {
        UUID postId = UUID.randomUUID();
        when(postRepository.findPostWithNumOfLikes(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPostFull(postId));
    }

    @Test
    public void PostService_getPost_AllGood_ReturnPostFullFull() {
        UUID postId = UUID.randomUUID();

        Post expected = Post.builder()
                .id(postId)
                .title("some title")
                .body("some body")
                .build();

        when(postRepository.findPostById(postId)).thenReturn(Optional.of(expected));
        Post result = postService.getPost(postId);

        assertEquals(expected, result);
    }

    @Test
    public void PostService_getPost_PostNotExisting_PostFullNotFoundException() {
        UUID postId = UUID.randomUUID();
        when(postRepository.findPostWithNumOfLikes(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPostFull(postId));
    }

    @Test
    public void PostService_deletePost_AllGood() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .email("email")
                .build();

        Post expected = Post.builder()
                .id(postId)
                .title("some title")
                .body("some body")
                .author(user)
                .build();

        when(postRepository.findPostByIdWithAuthor(postId)).thenReturn(Optional.of(expected));
        postService.deletePost(postId, user);
    }

    @Test
    public void PostService_deletePost_PostNotExisting_PostNotFoundException() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .email("email")
                .build();

        when(postRepository.findPostByIdWithAuthor(postId)).thenReturn(Optional.empty());
        assertThrows(PostNotFoundException.class, () -> postService.deletePost(postId, user));
    }

    @Test
    public void PostService_deletePost_UserNotOwner_UserNotOwnerException() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .email("email")
                .build();

        User owner = User.builder()
                .id(ownerId)
                .email("email")
                .build();

        Post expected = Post.builder()
                .id(postId)
                .title("some title")
                .body("some body")
                .author(user)
                .build();

        when(postRepository.findPostByIdWithAuthor(postId)).thenReturn(Optional.of(expected));
        postService.deletePost(postId, user);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 50",
            "2, 101",
            "-2, -101",
            "4, 0",
            "0, 100"
    })
    void PostService_getPostExplorePage_InvalidPageNumber_InvalidPaginationException(int pageNumber, int pageSize) {
        assertThrows(InvalidPaginationException.class, () -> postService.getPostExplorePage(pageNumber, pageSize, ""));
    }


}
