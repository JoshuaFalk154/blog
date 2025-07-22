package com.blog.blog.service;

import com.blog.blog.entities.Like;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.LikeAlreadyExistsException;
import com.blog.blog.exceptions.LikeNotFoundException;
import com.blog.blog.exceptions.PostNotFoundException;
import com.blog.blog.exceptions.UserNotExistingException;
import com.blog.blog.repository.LikeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final EntityManager entityManager;
    private final PostService postService;
    private final UserService userService;

    /**
     * Adds a like to the post
     * @param postId ID of the post to be liked
     * @param user User liking the post
     * @return The like added to the DB
     * @throws LikeAlreadyExistsException If the user already liked the post
     * @throws PostNotFoundException If a post with postId does not exist
     * @throws UserNotExistingException If user does not exist
     */
    @Transactional
    public Like addLike(UUID postId, User user) {
        if (likeRepository.existsByUserAndPostId(user, postId)) {
            throw new LikeAlreadyExistsException(String.format("User with email %s already liked post with id %s", user.getEmail(), postId));
        }

        if (!userService.userExists(user.getSub())) {
            throw new UserNotExistingException(String.format("User with sub %s does not exist", user.getSub()));
        }

        Post post = postService.getPost(postId);

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        user = entityManager.merge(user);

        post.addLike(like);
        user.addLike(like);

        return like;
    }

    /**
     * Deletes a like
     * @param postId ID of the liked post
     * @param user User who likes the post
     * @throws LikeNotFoundException If like from user of post with postId does not exist
     * @throws PostNotFoundException If post with postId does not exist
     * @throws UserNotExistingException If user does not exist
     */
    @Transactional
    public void deleteLike(UUID postId, User user) {
        final String userEmail = user.getEmail();
        Like like = likeRepository.findByUserAndPostId(user, postId)
                .orElseThrow(() ->  new LikeNotFoundException(String.format("Like from user with email %s of post with id %s does not exist", userEmail, postId)));

        if (!userService.userExists(user.getSub())) {
            throw new UserNotExistingException(String.format("User with sub %s does not exist", user.getSub()));
        }

        Post post = postService.getPost(postId);

        post.removeLike(like);

        user = entityManager.merge(user);
        user.removeLike(like);

        likeRepository.delete(like);
    }

}
