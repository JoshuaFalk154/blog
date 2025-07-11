package com.blog.blog.service;

import com.blog.blog.entities.Post;
import com.blog.blog.dto.PostCreate;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.PostNotFoundException;
import com.blog.blog.exceptions.UserNotExistingException;
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
     * Returns post by id
     *
     * @param postId Id of the post
     * @return Post with id=postId
     * @throws PostNotFoundException If the post with id postId does not exist
     */
    @Transactional
    public Post getPost(UUID postId) {
        return postRepository.findPostByIdWithAuthor(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found", postId)));
    }

    /**
     * Gets a page of posts with the given pageNumber and pageSize. It filters by the title of the post. It loads also the authors of each post.
     * @param pageNumber The number of the page
     * @param pageSize The number of posts in the page
     * @param titlePattern Filters all posts by the title containing the given pattern
     * @throws IllegalArgumentException If pageNumber or pageSize is invalid
     * @return A page of posts
     */
    @Transactional
    public Page<Post> getPostPageWithAuthor(int pageNumber, int pageSize, String titlePattern) {
        if (pageNumber <= 0) {
            throw new IllegalArgumentException("Page numbers can't be smaller than zero");
        }

        if (pageSize > 100) {
            throw new IllegalArgumentException("A page can't have more than 100 elements");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return postRepository.findAllByTitleContainingWithAuthor(titlePattern, pageable);
    }
}
