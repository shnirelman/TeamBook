package com.bls.TeamBook.repo;

import com.bls.TeamBook.models.Article;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.mapping.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(
            value = "SELECT id FROM Article a WHERE a.address_name = :address_name",
            nativeQuery = true
    )
    ArrayList<Long> findIdByAddressName(@Param("address_name") String addressName);
}

