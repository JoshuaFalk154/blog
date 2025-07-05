package com.blog.blog;

import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    final private UserService userService;
    final private UserRepository userRepository;
    final private PostRepository postRepository;
    final private EntityManager entityManager;

    //    @GetMapping("/test")
//    public String test(@AuthenticationPrincipal User user) {
//        Post post = Post.builder()
//                .title("sometitle")
//                .body("somebody")
//                .build();
//
//        user.setPosts(new ArrayList<>(List.of(post)));
//
//
//        userRepository.save(user);
//
//
//        SecurityContext x = SecurityContextHolder.getContext();
//        return "hello world";
//    }
    @GetMapping("/test")
    @Transactional
    public String test(@AuthenticationPrincipal User user) {
//        var user1 = User.builder()
//                .sub("somesub")
//                .username("somename")
//                .build();
//        var post = Post.builder()
//                .title("sometitle")
//                .author(user1)
//                .build();
//
//        //user1.addPost(post);
//        //user1.setPosts(new ArrayList<>(List.of(post)));
//
//        entityManager.persist(user1);
//        entityManager.persist(post);

        //entityManager.persist(post);
        return "hello world";
    }

    @GetMapping("/get")
    @Transactional
    public String get(@AuthenticationPrincipal User user) {
        var x = userRepository.findBySub("somesub");
        var y = x.get().getPosts();

        return "hello world";
    }
}
