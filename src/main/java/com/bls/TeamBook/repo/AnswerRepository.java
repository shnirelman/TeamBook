package com.bls.TeamBook.repo;

import com.bls.TeamBook.models.Answer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    @Query(
            value = "SELECT * FROM Answer a WHERE a.question_id in :questions_id",
            nativeQuery = true
    )
    ArrayList<Answer> findAllByQuestionsId(@Param("questions_id") ArrayList<Long> questionsId);
}
