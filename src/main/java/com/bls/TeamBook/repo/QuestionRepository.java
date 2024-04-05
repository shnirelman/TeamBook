package com.bls.TeamBook.repo;

import com.bls.TeamBook.models.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    @Query(
            value = "SELECT * FROM Question q WHERE q.article_id = :article_id",
            nativeQuery = true
    )
    ArrayList<Question> findAllByArticleId(@Param("article_id") Long articleId);
}
