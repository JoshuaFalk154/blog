package com.blog.blog.controllers;

import com.blog.blog.dto.*;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Add a post", description = "Creates a new post for the currently authenticated user")
    @PostMapping
    public ResponseEntity<PostCreated> addPost(@AuthenticationPrincipal User user, @Valid @RequestBody PostCreate postCreate) {
        Post post = postService.addPost(user, postCreate);
        PostCreated postCreated = new PostCreated(post.getTitle(), post.getId(), post.getCreatedAt(), post.getAuthor().getEmail());

        return new ResponseEntity<>(postCreated, HttpStatus.CREATED);
    }

    @Operation(summary = "Return a post by ID", description = "Returns a post by its ID. It also contains the author's email as well as the number of likes")
    @GetMapping("/{postId}")
    public ResponseEntity<PostFull> getPost(@AuthenticationPrincipal User user, @PathVariable UUID postId) {
        PostFull postFull = postService.getPostFull(postId);

        return new ResponseEntity<>(postFull, HttpStatus.OK);
    }


    @Operation(summary = "Return a page of posts",
            description = "Returns a page of posts. Each post contains the author's email as well as the number of likes. You can optionally filter posts by their title"
    )
    @GetMapping
    public ResponseEntity<PostPage<PostExplore>> getPostPage(
            @Parameter(description = "Page number to retrieve") @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNumber,
            @Parameter(description = "Number of posts per page") @RequestParam(value = "pageSize", defaultValue = "25", required = false) int pageSize,
            @Parameter(description = "Only return posts containing this string") @RequestParam(value = "titleSubstring", defaultValue = "", required = false) String titleSubstring
    ) {
        Page<PostExplore> postPage = postService.getPostExplorePage(pageNumber, Math.min(pageSize, 100), titleSubstring);

        PostPage<PostExplore> result = new PostPage<>
                (postPage.getContent(),
                        postPage.getNumber(),
                        postPage.getSize(),
                        postPage.getTotalElements(),
                        postPage.getTotalPages()
                );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "Delete a post by its ID ",
            description = "Deletion is only possible, if the authenticated user owns the post"
    )
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> getPostPage(
            @PathVariable("postId") UUID postId,
            @AuthenticationPrincipal User user
    ) {

        postService.deletePost(postId, user);

        return new ResponseEntity<>(String.format("Post with id %s deleted", postId), HttpStatus.OK);
    }

    // TODO get all posts of a user
}

