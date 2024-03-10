package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.models.Answer;
import com.bls.TeamBook.models.MyUser;
import com.bls.TeamBook.models.Question;
import com.bls.TeamBook.repo.AnswerRepository;
import com.bls.TeamBook.repo.QuestionRepository;
import com.bls.TeamBook.services.MainService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class MainController {
    private MainService service;

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

    @PostMapping("/new-user")
    public String addUser(@RequestParam String login, @RequestParam String password,
                          HttpServletRequest request, Model model) throws ServletException {
        service.addUser(new MyUser(login, password));

        request.logout();
        request.login(login, password);
        return "redirect:/";
    }

    @GetMapping("/new-user")
    public String newUser(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

        return "new_user";
    }
}
