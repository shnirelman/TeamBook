package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.models.Question;
import com.bls.TeamBook.repo.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "World");
        return "home";
    }

    @GetMapping("/article/{name}")
    public String home(@PathVariable(value = "name") String name, Model model) {
        model.addAttribute("name", name);
        return "article";
    }

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/test/{name}")
    public String test(@PathVariable(value = "name") String name, Model model) {
        model.addAttribute("name", name);
        Iterable<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        return "test";
    }
}
