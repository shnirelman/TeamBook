package com.bls.TeamBook.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.lang.NonNull;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long article_id;
    private Long user_id;
    private Long parent_id;
    private int level;
    @NonNull
    private Timestamp date;

    private String text_md;


    private String text_html;

    public Long getUser_id() {
        return user_id;
    }

    public Comment() {}
    public Comment(Long article_id, Long user_id, Long parent_id, String text_md) {
        this.article_id = article_id;
        this.user_id = user_id;
        this.parent_id = parent_id;
        this.text_md = text_md;
        this.text_html = null;
        this.date = Timestamp.from(Instant.now());
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Long article_id) {
        this.article_id = article_id;
    }

    public String getText_md() {
        return text_md;
    }

    public String getText_html() {
        return text_html;
    }

    public void setText_md(String text_md) {
        this.text_md = text_md;
    }

    public void setText_html(String text_html) {
        this.text_html = text_html;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
