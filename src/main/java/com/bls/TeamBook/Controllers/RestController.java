package com.bls.TeamBook.Controllers;


import com.bls.TeamBook.models.Comment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
public class RestController extends Controller {
    @GetMapping("/getComments")
    public String getComments(Model model) {
        return "abacaba";
    }

    @PostMapping("/article/{articleName}/new_comment")
    public CommentOutputInfo addComment(@PathVariable(value = "articleName") String articleName,
                             @RequestParam String comment_text,
                             @RequestParam Long par_id,
                             @RequestParam int level,
                             Model model) {

        System.out.println("add comment");
        /*return "home";*/
        Long articleId = getArticleId(articleName);

        Long userId = (getLogin().isBlank() ? -1 : commentRepository.findIdByLogin(getLogin()).get(0));
        Comment comment = new Comment(articleId, userId, par_id, comment_text);
        commentService.addComment(comment);
        //return "home";
        return new CommentOutputInfo(userRepository, comment, level);
        //return "redirect: /article/" + articleName;
        //return "redirect:/";
    }
}
