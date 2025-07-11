package com.blog.blog.controllers;

import com.blog.blog.dto.PostCreate;
import com.blog.blog.dto.PostCreated;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "Operations related to posts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    final private PostService postService;

    @Operation(summary = "Add a post", description = "Add a post to the currently logged in user")
    @PostMapping
    public ResponseEntity<PostCreated> addPost(@AuthenticationPrincipal User user, @Valid @RequestBody  PostCreate postCreate) {
        Post post = postService.addPost(user, postCreate);
        PostCreated postCreated = new PostCreated(post.getId(), post.getCreatedAt(), post.getAuthor().getEmail());

        return new ResponseEntity<>(postCreated, HttpStatus.CREATED);
    }

}

