package com.blog.blog.service;

import com.blog.blog.dto.*;
import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.*;
import com.blog.blog.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    final private EntityManager entityManager;
    final private Validator validator;
    final private UserService userService;
    final private PostRepository postRepository;


    /**
     * Adds the post to the provided user.
     *
     * @param user       User to whom to add the post.
     * @param postCreate Post to be added.
     * @return Post after added to database.
     * @throws PostIllegalArgumentException If Post has invalid values.
     * @throws UserNotExistingException If User does not exist.
     */
    @Transactional
    public Post addPost(User user, @Valid PostCreate postCreate) {
        Set<ConstraintViolation<PostCreate>> violations = validator.validate(postCreate);

        if (!violations.isEmpty()) {
            throw new PostIllegalArgumentException("Illegal arguments for post");
        }

        if (!userService.userExists(user.getSub())) {
            throw new UserNotExistingException(String.format("User with sub %s does not exist", user.getSub()));
        }

        Post post = Post.builder()
                .title(postCreate.title())
                .body(postCreate.body())
                .build();

        user = entityManager.merge(user);
        user.addPost(post);

        return post;
    }

    /**
     * Returns PostFull by ID
     *
     * @param postId ID of the post
     * @return PostFull with postId as ID
     * @throws PostNotFoundException If a post with id postId does not exist
     */
    @Transactional
    public PostFull getPostFull(UUID postId) {
        return postRepository.findPostWithNumOfLikes(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found", postId)));
    }

    /**
     * Returns post by ID
     *
     * @param postId ID of the post
     * @return Post with postId as ID
     * @throws PostNotFoundException If the post with ID postId does not exist
     */
    @Transactional
    public Post getPost(UUID postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found", postId)));
    }


    /**
     * Returns a Page of PostExplore objects.
     *
     * @param pageNumber   The number of the page - Starting from 1
     * @param pageSize     The number of posts in the page - maximum is 100
     * @param titlePattern Filters all posts by the title containing the given pattern
     * @return A page of posts
     * @throws InvalidPaginationException If pageNumber is <= 0 or pageSize is out of range
     */
    @Transactional
    public Page<PostExplore> getPostExplorePage(int pageNumber, int pageSize, String titlePattern) {
        if (pageNumber <= 0) {
            throw new InvalidPaginationException("Page numbers can't be smaller than zero");
        }

        if (pageSize <= 0 || pageSize > 100) {
            throw new InvalidPaginationException("A page can't have more than 100 elements and less than 1");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return postRepository.findAllWithAuthorAndNumOfLikes(titlePattern, pageable);
    }

    /**
     * Deletes a post
     *
     * @param postId ID of the post
     * @param user   The user who is supposed to own the post
     * @throws PostNotFoundException If a post with postId doesn't exist
     * @throws UserNotExistingException If User does not exist.
     * @throws UserNotOwnerException If the user is not the owner of the post
     */
    @Transactional
    public void deletePost(UUID postId, User user) {
        Post post = postRepository.findPostByIdWithAuthor(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s does not exist", postId)));

        if (!userService.userExists(user.getSub())) {
            throw new UserNotExistingException(String.format("User with sub %s does not exist", user.getSub()));
        }

        if (!post.getAuthor().equals(user)) {
            throw new UserNotOwnerException(String.format("User with email %s is not owner of post with id %s", user.getEmail(), postId));
        }

        post.remove();
        entityManager.merge(user);
        postRepository.deleteById(postId);
    }

    /**
     * Updates a post by its ID. Creates a new post, if a post with postId does not already exist
     *
     * @param postId     ID of the post
     * @param postUpdate Used to update the post
     * @param user       Owner of the post
     * @return Post and status CREATED or UPDATED
     * @throws IllegalArgumentException If postUpdate is not valid
     * @throws UserNotExistingException If user does not exist
     * @throws UserNotOwnerException    If post exists but user is not the owner
     */
    @Transactional
    public PostPut updatePost(UUID postId, PostUpdate postUpdate, User user) {
        Set<ConstraintViolation<PostUpdate>> violations = validator.validate(postUpdate);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Illegal arguments for post");
        }

        if (!userService.userExists(user.getSub())) {
            throw new UserNotExistingException(String.format("User with sub %s does not exist", user.getSub()));
        }

        Optional<Post> postOptional = postRepository.findPostByIdWithAuthor(postId);
        Post post;

        if (postOptional.isPresent()) {
            post = postOptional.get();

            if (!post.getAuthor().equals(user)) {
                throw new UserNotOwnerException(String.format("User with email %s does not own post with id %s", user.getEmail(), postId));
            }

            post.setTitle(postUpdate.title());
            post.setBody(postUpdate.body());

            return new PostPut(post, PutResponse.UPDATED);
        } else {
            post = Post.builder()
                    .id(postId)
                    .author(user)
                    .title(postUpdate.title())
                    .body(postUpdate.body())
                    .build();

            user = entityManager.merge(user);
            user.addPost(post);
            return new PostPut(post, PutResponse.CREATED);
        }

    }
}
