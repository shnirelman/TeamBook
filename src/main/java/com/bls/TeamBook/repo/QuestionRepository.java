package com.bls.TeamBook.repo;

import com.bls.TeamBook.models.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> {
}
