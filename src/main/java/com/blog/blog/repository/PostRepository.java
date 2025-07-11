package com.blog.blog.repository;

import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findByAuthor(User user);

    @Query(value = "SELECT p, a FROM Post p JOIN p.author a WHERE p.id = ?1")
    Optional<Post> findPostByIdWithAuthor(UUID id);

    @Query(value = """
            SELECT p
            FROM Post p
            JOIN FETCH p.author a
            WHERE p.title LIKE %:titlePattern%
            """,
            countQuery = """
                    SELECT count(p)
                    FROM Post p
                    WHERE p.title LIKE %:titlePattern%
                    """)
    Page<Post> findAllByTitleContainingWithAuthor(@Param("titlePattern") String titlePattern, Pageable pageable);
}
