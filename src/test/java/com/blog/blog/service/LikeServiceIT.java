package com.blog.blog.service;

import com.blog.blog.MyTestcontainersConfiguration;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.repository.LikeRepository;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(MyTestcontainersConfiguration.class)
public class LikeServiceIT {

    @Autowired
    LikeService likeService;

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
        likeRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void LikeService_addLike_LikeAdded() {
        Post post = Post.builder()
                .title("title")
                .body("some body with a text ")
                .author(user)
                .build();

        post = postRepository.save(post);
        likeService.addLike(post.getId(), user);


        assertThat(likeRepository.findByUserAndPostId(user, post.getId())).isPresent();
    }

    @Test
    void LikeService_deleteLike_LikeDeleted() {
        Post post = Post.builder()
                .title("title")
                .body("some body with a text ")
                .author(user)
                .build();

        post = postRepository.save(post);
        likeService.addLike(post.getId(), user);

        assertThat(likeRepository.findByUserAndPostId(user, post.getId())).isPresent();

        likeService.deleteLike(post.getId(), user);

        assertThat(likeRepository.findByUserAndPostId(user, post.getId())).isEmpty();
    }

}
