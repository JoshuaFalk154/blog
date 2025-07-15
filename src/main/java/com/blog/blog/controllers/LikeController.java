package com.blog.blog.controllers;

import com.blog.blog.dto.LikeCreated;
import com.blog.blog.entities.Like;
import com.blog.blog.entities.User;
import com.blog.blog.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Like", description = "Operations related to likes")
@RequiredArgsConstructor
@RestController
@RequestMapping("posts/likes")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "Like a post", description = "Like a post by its ID")
    @PostMapping("/{postId}")
    public ResponseEntity<?> addLike(@AuthenticationPrincipal User user, @PathVariable("postId") UUID postId) {
        Like like = likeService.addLike(postId, user);

        LikeCreated likeCreated = new LikeCreated(like.getId(), like.getCreatedAt(), like.getUser().getId(), like.getPost().getId());

        return new ResponseEntity<>(likeCreated, HttpStatus.CREATED);
    }

    @Operation(summary = "Unlike a post", description = "Unlike a post by its ID")
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deleteLike(@AuthenticationPrincipal User user, @PathVariable("postId") UUID postId) {
        likeService.deleteLike(postId, user);

        return new ResponseEntity<>(String.format("Like from %s of post %s deleted", user.getEmail(), postId), HttpStatus.OK);
    }

}
