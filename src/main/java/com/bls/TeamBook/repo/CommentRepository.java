package com.bls.TeamBook.repo;

import com.bls.TeamBook.models.Answer;
import com.bls.TeamBook.models.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Query(
            value = "SELECT * FROM Comment c WHERE c.article_id = :article_id",
            nativeQuery = true
    )
    ArrayList<Comment> getCommentsByArticleId(@Param("article_id") Long articleId);

    @Query(
            value = "SELECT id FROM users WHERE login = :login",
            nativeQuery = true
    )
    ArrayList<Long> findIdByLogin(@Param("login") String login);

    @Query(
            value = "SELECT level FROM comment WHERE id = :id",
            nativeQuery = true
    )
    Optional<Integer> findLevelById(@Param("id") Long id);

    @Query(
            value = "SELECT * FROM Comment c WHERE c.date >= :date",
            nativeQuery = true
    )
    ArrayList<Comment> getCommentsByDate(@Param("date") Timestamp date);
}
