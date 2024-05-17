package com.bls.TeamBook.repo;

import com.bls.TeamBook.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    @Query
    Iterable<Chapter> findAllByArticleId(long articleId);
}
