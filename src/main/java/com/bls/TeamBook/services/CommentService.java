package com.bls.TeamBook.services;

import com.bls.TeamBook.models.Comment;
import com.bls.TeamBook.models.MyUser;
import com.bls.TeamBook.repo.CommentRepository;
import com.bls.TeamBook.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }
}
