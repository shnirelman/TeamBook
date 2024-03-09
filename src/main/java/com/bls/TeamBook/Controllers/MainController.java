package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.models.Answer;
import com.bls.TeamBook.models.Question;
import com.bls.TeamBook.repo.AnswerRepository;
import com.bls.TeamBook.repo.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/test/{name}")
    public String test(@PathVariable(value = "name") String name, Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

        model.addAttribute("name", name);
        Iterable<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        Iterable<Answer> answers = answerRepository.findAll();
        model.addAttribute("answers", answers);
        return "test";
    }
}
