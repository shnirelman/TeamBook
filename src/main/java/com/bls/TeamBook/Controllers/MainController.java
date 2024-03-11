package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.models.Answer;
import com.bls.TeamBook.models.MyUser;
import com.bls.TeamBook.models.Question;
import com.bls.TeamBook.repo.AnswerRepository;
import com.bls.TeamBook.repo.QuestionRepository;
import com.bls.TeamBook.repo.UserRepository;
import com.bls.TeamBook.services.MainService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class MainController {
    private MainService service;
    public String getLogin() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return login;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("login", getLogin());

        return "home";
    }

    @GetMapping("/tests")
    public String tests(Model model) {
        model.addAttribute("login", getLogin());

        return "tests";
    }

    @GetMapping("/article/{name}")
    public String home(@PathVariable(value = "name") String name, Model model) {
        model.addAttribute("login", getLogin());

        model.addAttribute("name", name);
        return "article";
    }

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test/{name}")
    public String test(@PathVariable(value = "name") String name, Model model) {
        model.addAttribute("login", getLogin());

        model.addAttribute("name", name);
        Iterable<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        Iterable<Answer> answers = answerRepository.findAll();
        model.addAttribute("answers", answers);
        return "test";
    }

    @PostMapping("/new-user")
    public String addUser(@RequestParam String login, @RequestParam String password, @RequestParam String confirmPassword,
                          HttpServletRequest request, Model model) throws ServletException {
        if(userRepository.findByLogin(login).isPresent()) {
            model.addAttribute("loginError", "Такой пользователь уже есть");
            model.addAttribute("login", getLogin());

            return "new_user";
        }

        int len = login.length();

        if(len == 0 || len > 20) {
            model.addAttribute("loginError", "Логин должен быть от 1 до 20 символов");
            model.addAttribute("login", getLogin());

            return "new_user";
        }

        if(password.length() < 8) {
            model.addAttribute("passwordError", "Пароль должен быть не менее 8 символов");
            model.addAttribute("login", getLogin());

            return "new_user";
        }

        if(!Objects.equals(confirmPassword, password)) {
            model.addAttribute("confirmPasswordError", "Пароли не совпадают");
            model.addAttribute("login", getLogin());

            return "new_user";
        }

        service.addUser(new MyUser(login, password));

        request.logout();
        request.login(login, password);
        return "redirect:/";
    }

    @GetMapping("/new-user")
    public String newUser(Model model) {
        model.addAttribute("login", getLogin());

        return "new_user";
    }
}
