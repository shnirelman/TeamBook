package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.models.*;
import com.bls.TeamBook.repo.*;
import com.bls.TeamBook.services.CommentService;
import com.bls.TeamBook.services.MainService;
import com.bls.TeamBook.services.MyUserDetailsService;
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

import java.util.*;

@Controller
@AllArgsConstructor
public class MainController extends com.bls.TeamBook.Controllers.Controller {
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



    class CommentComparator implements Comparator<Comment> {
        @Override
        public int compare(Comment c1, Comment c2) {
            return c1.getDate().compareTo(c2.getDate());
        }
    }

    void commentsDFS(Comment comment,
                     int level,
                     HashMap<Long, ArrayList<Comment>> graph,
                     ArrayList<CommentOutputInfo> result) {
        ArrayList<Comment> children = graph.get(comment.getId());
        Collections.sort(children, new CommentComparator());
        result.add(new CommentOutputInfo(userRepository, comment, level));

        for(Comment child : children) {
            commentsDFS(child, Math.min(level + 1, CommentOutputInfo.maxLevel), graph, result);
        }
    }

    @GetMapping("/article/{name}")
    public String home(@PathVariable(value = "name") String name, Model model) {
        model.addAttribute("login", getLogin());

        Long id = getArticleId(name);

        model.addAttribute("name", name);
        model.addAttribute("id", id);

        ArrayList<Comment> comments = commentRepository.getCommentsByArticleId(id);
        HashMap<Long, ArrayList<Comment>> graph = new HashMap<>();
        ArrayList<Comment> roots = new ArrayList<>();
        for(Comment comment : comments) {
            Long parId = comment.getParent_id();
            if(parId != null && parId >= 0) {
                graph.get(parId).add(comment);
            } else {
                roots.add(comment);
            }
            graph.put(comment.getId(), new ArrayList<Comment>());
        }

        ArrayList<CommentOutputInfo> commentsOutputInfo = new ArrayList<>();
        Collections.sort(roots, new CommentComparator());
        for(Comment root : roots) {
            commentsDFS(root, 0, graph, commentsOutputInfo);
        }

        model.addAttribute("comments", commentsOutputInfo);

        Iterable<Chapter> chapters = chapterRepository.findAllByArticleId(id);
        model.addAttribute("chapters", chapters);

        System.out.println("----------------------- id = " + id);
        for(Chapter c : chapters) {
            System.out.println("chapter + " + c.getId() + " " + c.getName() + "  " + c.getCode() + " " + c.getArticleId());
        }

        return "article";
    }

    @GetMapping("/test/{name}")
    public String test(@PathVariable(value = "name") String name, Model model) {
        model.addAttribute("login", getLogin());

        Long id = getArticleId(name);

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





    @PostMapping("/new-user")
    public String addUser(@RequestParam String login, @RequestParam String password, @RequestParam String confirmPassword,
                          HttpServletRequest request, Model model) throws ServletException {
        boolean error = false;

        if(userRepository.findByLogin(login).isPresent()) {
            model.addAttribute("loginError", "Такой пользователь уже есть");
            error = true;
        }

        int len = login.length();

        if(len < 3 || len > 20) {
            model.addAttribute("loginError", "Логин должен быть от 3 до 20 символов");
            error = true;
        }

        if(password.length() < 8) {
            model.addAttribute("passwordError", "Пароль должен быть не менее 8 символов");
            error = true;
        }

        if(!Objects.equals(confirmPassword, password)) {
            model.addAttribute("confirmPasswordError", "Пароли не совпадают");
            error = true;
        }

        if(error) {
            model.addAttribute("login", getLogin());
            model.addAttribute("savedLogin", login);
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

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", getLogin());

        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        return "redirect:/";
    }
}
