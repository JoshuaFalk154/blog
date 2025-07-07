package com.blog.blog.controllers;

import com.blog.blog.dto.PostCreate;
import com.blog.blog.dto.PostCreated;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    final private PostService postService;

    @PostMapping
    public ResponseEntity<PostCreated> addPost(@AuthenticationPrincipal User user, @Valid @RequestBody  PostCreate postCreate) {
        Post post = postService.addPost(user, postCreate);
        PostCreated postCreated = new PostCreated(post.getId(), post.getCreatedAt());

        return new ResponseEntity<>(postCreated, HttpStatus.CREATED);
    }

}

