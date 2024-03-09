package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.models.Answer;
import com.bls.TeamBook.models.Article;
import com.bls.TeamBook.models.Question;
import com.bls.TeamBook.repo.AnswerRepository;
import com.bls.TeamBook.repo.QuestionRepository;
import com.bls.TeamBook.repo.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

        return "home";
    }

    @GetMapping("/tests")
    public String tests(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

        return "tests";
    }

    @GetMapping("/article/{name}")
    public String home(@PathVariable(value = "name") String name, Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

        model.addAttribute("name", name);
        return "article";
    }

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/test/{name}")
    public String test(@PathVariable(value = "name") String name, Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

        // Iterable<Answer> ids = answerRepository.testing();
        //Iterable<Long> ids = articleRepository.findIdByAddressName("prefix_sums");
        Iterable<Long> ids = articleRepository.findIdByAddressName(name);
        Iterator<Long> idsItrator = ids.iterator();
        if(!idsItrator.hasNext()) {
            //TODO ERROR
        }
        Long id = idsItrator.next();


        model.addAttribute("name", name);
        model.addAttribute("id", id);

        ArrayList<Question> questions = questionRepository.findAllByArticleId(id);
        model.addAttribute("questions", questions);

        ArrayList<Long> questionsId = new ArrayList<Long>();
        for(Question q : questions) {
            questionsId.add(q.getId());
        }
        Iterable<Answer> answers = answerRepository.findAllByQuestionsId(questionsId);
        model.addAttribute("answers", answers);/**/
        return "test";
    }
}
