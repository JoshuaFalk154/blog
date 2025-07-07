package com.blog.blog.service;

import com.blog.blog.entities.Post;
import com.blog.blog.dto.PostCreate;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.UserNotExistingException;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    final private EntityManager entityManager;
    final private Validator validator;
    final private UserService userService;


    /**
     * Adds the post to the provided user.
     * @param user User to whom to add the post.
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

}
