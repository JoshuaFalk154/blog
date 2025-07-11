package com.blog.blog.controllers;

import com.blog.blog.dto.*;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Post", description = "Operations related to posts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    final private PostService postService;

    @Operation(summary = "Add a post", description = "Add a post to the currently logged in user")
    @PostMapping
    public ResponseEntity<PostCreated> addPost(@AuthenticationPrincipal User user, @Valid @RequestBody PostCreate postCreate) {
        Post post = postService.addPost(user, postCreate);
        PostCreated postCreated = new PostCreated(post.getTitle(), post.getId(), post.getCreatedAt(), post.getAuthor().getEmail());

        return new ResponseEntity<>(postCreated, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a post", description = "Get a post by it's id")
    @GetMapping("/{postId}")
    public ResponseEntity<PostSingle> getPost(@AuthenticationPrincipal User user, @PathVariable UUID postId) {
        Post post = postService.getPost(postId);
        PostSingle result = new PostSingle(post.getTitle(), post.getBody(), post.getAuthor().getEmail(), post.getCreatedAt(), post.getUpdatedAt());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Get posts", description = "Get the posts in the specified page with specified number of posts for each page")
    @GetMapping
    public ResponseEntity<PostPage<PostExplore>> getPosts(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "25", required = false) int pageSize,
            @RequestParam(value = "contains", defaultValue = "", required = false) String contains
    ) {
        Page<Post> postPage = postService.getPostPageWithAuthor(pageNumber, Math.min(pageSize, 100), contains);

        PostPage<PostExplore> result = new PostPage<>
                (postPage.getContent().stream()
                        .map(post -> new PostExplore(post.getTitle(), post.getId(), post.getCreatedAt(), post.getAuthor().getEmail())).toList(),
                        postPage.getNumber(),
                        postPage.getSize(),
                        postPage.getTotalElements(),
                        postPage.getTotalPages()
                );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

