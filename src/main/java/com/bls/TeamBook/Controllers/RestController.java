package com.bls.TeamBook.Controllers;


import com.bls.TeamBook.Exception.EntityNotFoundException;
import com.bls.TeamBook.models.Comment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController extends Controller {
    @GetMapping("/getComments")
    public String getComments(Model model) {
        return "abacaba";
    }

    @PostMapping("/article/{articleName}/new_comment")
    public List<CommentOutputInfo> addComment(@PathVariable(value = "articleName") String articleName,
                             @RequestBody String comment_text,
                             @RequestParam Long par_id,
                             @RequestParam String date,
                             Model model) throws EntityNotFoundException {


        System.out.println("add comment");
        System.out.println("comment_text = " + comment_text);
        System.out.println("par_id = " + par_id);
        System.out.println("date = " + date);
        System.out.println("cur date = " + Timestamp.from(Instant.now()).toString());
        /*return "home";*/
        Long articleId = getArticleId(articleName);

        Long userId = (getLogin().isBlank() ? -1 : commentRepository.findIdByLogin(getLogin()).get(0));
        Comment comment = new Comment(articleId, userId, par_id, comment_text);
        commentService.addComment(comment);
        //return "home";
        //return new CommentOutputInfo(userRepository, comment, comment.getLevel());

        //return "redirect: /article/" + articleName;
        //return "redi rect:/";
        //model.addAttribute("date", Timestamp.from(Instant.now()).toString());
        List<CommentOutputInfo> res = comentsToCommnetsOutputInfo(commentService.getCommentsFromDate(Timestamp.valueOf(date)));
        return res;
    }

    @GetMapping("/article/{articleName}/new_comments")
    public List<CommentOutputInfo> getCommentsFromDate(@PathVariable(value = "articleName") String articleName,
                                                            @RequestParam String date,
                                                            Model model) throws EntityNotFoundException {
        List<CommentOutputInfo> res = comentsToCommnetsOutputInfo(commentService.getCommentsFromDate(Timestamp.valueOf(date)));
        System.out.println("res.size() = " + res.size());
        return res;
    }



    private ArrayList<CommentOutputInfo> comentsToCommnetsOutputInfo(ArrayList<Comment> comments) {
        ArrayList<CommentOutputInfo> res = new ArrayList<>();
        for(Comment c : comments) {
            res.add(new CommentOutputInfo(userRepository, c, c.getLevel()));
        }
        return res;
    }
}
