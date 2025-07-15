package com.blog.blog.service;

import com.blog.blog.MyTestcontainersConfiguration;
import com.blog.blog.dto.PostCreate;
import com.blog.blog.dto.UserLoad;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.repository.LikeRepository;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.security.SecurityConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MyTestcontainersConfiguration.class)
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ActiveProfiles("test")
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

    //@Transactional
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
}
