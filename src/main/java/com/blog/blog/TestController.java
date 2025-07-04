package com.blog.blog;

import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.service.UserService;
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

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal User user) {
        Post post = Post.builder()
                .title("sometitle")
                .body("somebody")
                .build();

        user.setPosts(new ArrayList<>(List.of(post)));

        userRepository.save(user);


        SecurityContext x = SecurityContextHolder.getContext();
        return "hello world";
    }
}
