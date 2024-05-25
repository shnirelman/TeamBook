package com.bls.TeamBook.services;

import com.bls.TeamBook.Exception.EntityNotFoundException;
import com.bls.TeamBook.models.Comment;
import com.bls.TeamBook.models.MyUser;
import com.bls.TeamBook.repo.CommentRepository;
import com.bls.TeamBook.repo.UserRepository;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;

    public void addComment(Comment comment) throws EntityNotFoundException {
        if(comment.getParent_id() == -1) {
            comment.setLevel(0);
        } else {
            Optional<Integer> parLevel = commentRepository.findLevelById(comment.getParent_id());
            if(parLevel.isEmpty())
                throw new EntityNotFoundException();
            comment.setLevel(parLevel.get() + 1);
        }

        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(comment.getText_md());
        String html = renderer.render(document);
        comment.setText_html(html);

        commentRepository.save(comment);
    }

    public ArrayList<Comment> getCommentsFromDate(Timestamp date) {
        return commentRepository.getCommentsByDate(date);
    }
}
