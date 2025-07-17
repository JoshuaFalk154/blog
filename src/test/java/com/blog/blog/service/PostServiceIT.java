package com.blog.blog.service;

import com.blog.blog.MyTestcontainersConfiguration;
import com.blog.blog.dto.PostCreate;
import com.blog.blog.dto.PostExplore;
import com.blog.blog.dto.PostFull;

import com.blog.blog.entities.Like;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.repository.LikeRepository;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MyTestcontainersConfiguration.class)
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
//@ActiveProfiles({"test", "test12"})
@ActiveProfiles({"test"})
public class PostServiceIT {

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    LikeRepository likeRepository;

    User user;

    @BeforeEach
    void beforeEach() {
        User toSave = User.builder()
                .sub("somesub")
                .email("mail@mail.com")
                .build();

        user = userRepository.save(toSave);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        postRepository.deleteAll();
        likeRepository.deleteAll();
    }

    @Test
    void PostService_addPost_PostAdded() {
        PostCreate postCreate = new PostCreate("This is an title", "This is the body");

        Post result = postService.addPost(user, postCreate);
        Optional<Post> expectedOptional = postRepository.findPostByIdWithAuthor(result.getId());

        assertNotNull(result);
        assertThat(expectedOptional).isPresent();
        assertEquals(result, expectedOptional.get());
        assertEquals(expectedOptional.get().getAuthor(), user);
    }

    @Test
    void PostService_getPost() {
        Post expected = Post.builder()
                .title("title")
                .body("some body with a text ")
                .author(user)
                .build();
        postRepository.save(expected);
        postRepository.flush();

        Post result = postService.getPost(expected.getId());

        assertEquals(result, expected);
    }

    @Test
    void PostService_getPostFull() {
        Post post = Post.builder()
                .title("title")
                .body("some body with a text ")
                .author(user)
                .build();
        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        post.addLike(like);
        post = postRepository.save(post);


        PostFull expected = new PostFull(post.getTitle(), post.getBody(), post.getId(), user.getEmail(), post.getCreatedAt(), post.getUpdatedAt(), 1L);

        PostFull result = postService.getPostFull(post.getId());

        assertEquals(expected, result);
    }



    @Test
    void PostService_deletePage() {
        Post post = Post.builder()
                .title("title")
                .body("some body with a text ")
                .author(user)
                .build();
        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        post.addLike(like);
        post = postRepository.save(post);
        Optional<Post> postOptional = postRepository.findPostById(post.getId());
        assertThat(postOptional).isPresent();

        postService.deletePost(post.getId(), user);

        postOptional = postRepository.findPostById(post.getId());
        assertThat(postOptional).isEmpty();
    }

    @Test
    void PostService_getPostExplorePage() {
        User user2 = User.builder()
                .sub("user2sub")
                .email("user2@mail.com")
                .build();
        userRepository.save(user2);

        Post post1 = Post.builder()
                .title("post1title")
                .body("post1body")
                .author(user)
                .build();
        Post post2 = Post.builder()
                .title("post2title")
                .body("post2body")
                .author(user)
                .build();
        Post post3 = Post.builder()
                .title("post3title")
                .body("post3body")
                .author(user2)
                .build();

        Like like1 = Like.builder()
                .post(post1)
                .user(user2)
                .build();

        post1.addLike(like1);
        postRepository.saveAll(List.of(post1, post2, post3));

        List<PostExplore> postExplores = new ArrayList<>(List.of(
           new PostExplore(post1.getTitle(), post1.getId(), post1.getCreatedAt(), post1.getAuthor().getEmail(), 1L),
                new PostExplore(post2.getTitle(), post2.getId(), post2.getCreatedAt(), post2.getAuthor().getEmail(), 0L),
                new PostExplore(post3.getTitle(), post3.getId(), post3.getCreatedAt(), post3.getAuthor().getEmail(), 0L)
        ));
        Page<PostExplore> expected = new PageImpl<PostExplore>(postExplores);

        Page<PostExplore> result = postService.getPostExplorePage(1, 5, "");

        assertTrue(expected.getContent().size() == result.getContent().size() &&
                expected.getContent().containsAll(result.getContent()) && result.getContent().containsAll(expected.getContent()));

    }

}
