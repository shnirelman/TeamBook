package com.bls.TeamBook.repo;

import com.bls.TeamBook.models.Answer;
import com.bls.TeamBook.models.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

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
}
