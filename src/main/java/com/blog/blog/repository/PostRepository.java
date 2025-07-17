package com.blog.blog.repository;

import com.blog.blog.dto.PostExplore;
import com.blog.blog.dto.PostFull;

import com.blog.blog.entities.Post;
import com.blog.blog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findByAuthor(User user);

    Optional<Post> findPostById(UUID id);

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

    @Query(value = """
            SELECT new com.blog.blog.dto.PostExplore(
                p.title as title,
                p.id as id,
                p.createdAt as createdAt,
                a.email as authorEmail,
                COUNT(l.id) as numberOfLikes
            )
            FROM Post p
            JOIN p.author a
            LEFT JOIN p.likes l
            WHERE p.title LIKE %:titlePattern%
            GROUP BY p.title, p.id, p.createdAt, a.email
            """,
            countQuery = """
                    SELECT count(p)
                    FROM Post p
                    WHERE p.title LIKE %:titlePattern%
                    """)
    Page<PostExplore> findAllWithAuthorAndNumOfLikes(@Param("titlePattern") String titlePattern, Pageable pageable);

    @Query(value = """
            SELECT new com.blog.blog.dto.PostFull(
                p.title as title,
                p.body as body,
                p.id as id,
                a.email as authorEmail,
                p.createdAt as createdAt,
                p.updatedAt as updatedAt,
                COUNT(l.id) as numberOfLikes
            )
            FROM Post p
            JOIN p.author a
            LEFT JOIN p.likes l
            WHERE p.id = :postId
            GROUP BY p.title, p.id, p.createdAt, a.email
            """,
            countQuery = """
                    SELECT count(p)
                    FROM Post p
                    WHERE p.id = :postId
                    """)
    Optional<PostFull> findPostWithNumOfLikes(@Param("postId") UUID postId);

}
