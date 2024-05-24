package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.repo.*;
import com.bls.TeamBook.services.CommentService;
import com.bls.TeamBook.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Iterator;

public class Controller {
    @Autowired
    protected ArticleRepository articleRepository;
    @Autowired
    protected QuestionRepository questionRepository;
    @Autowired
    protected AnswerRepository answerRepository;
    @Autowired
    protected ChapterRepository chapterRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected MainService service;
    @Autowired
    protected CommentService commentService;

    public String getLogin() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser")
            login = "";

        return login;
    }

    public Long getArticleId(String articleName) {
        Iterable<Long> ids = articleRepository.findIdByAddressName(articleName);
        Iterator<Long> idsItrator = ids.iterator();
        if(!idsItrator.hasNext()) {
            //TODO ERROR
        }
        return idsItrator.next();
    }
}
