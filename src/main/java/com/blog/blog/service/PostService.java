package com.blog.blog.service;

import com.blog.blog.dto.PostExplore;
import com.blog.blog.dto.PostFull;
import com.blog.blog.entities.Post;
import com.blog.blog.dto.PostCreate;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.InvalidPaginationException;
import com.blog.blog.exceptions.PostNotFoundException;
import com.blog.blog.exceptions.UserNotExistingException;
import com.blog.blog.exceptions.UserNotOwnerException;
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
     * @throws IllegalArgumentException If Post has invalid values.
     * @throws UserNotExistingException If User does not exist.
     */
    @Transactional
    public Post addPost(User user, @Valid PostCreate postCreate) {
        Set<ConstraintViolation<PostCreate>> violations = validator.validate(postCreate);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Illegal arguments for post");
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
     * @param postId ID of the post
     * @param user The user who is supposed to own the post
     * @throws PostNotFoundException If a post with postId doesn't exist
     * @throws UserNotOwnerException If the user is not the owner of the post
     */
    @Transactional
    public void deletePost(UUID postId, User user) {
        Post post = postRepository.findPostByIdWithAuthor(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s does not exist", postId)));

        if (!post.getAuthor().equals(user)) {
            throw new UserNotOwnerException(String.format("User with email %s is not owner of post with id %s", user.getEmail(), postId));
        }

        post.getLikes();
        post.remove();
        postRepository.deleteById(postId);
    }

}
