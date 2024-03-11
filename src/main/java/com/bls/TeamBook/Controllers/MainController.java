package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.models.Answer;
import com.bls.TeamBook.models.Article;
import com.bls.TeamBook.models.Comment;
import com.bls.TeamBook.models.Question;
import com.bls.TeamBook.repo.AnswerRepository;
import com.bls.TeamBook.repo.CommentRepository;
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

import java.util.*;

@Controller
public class MainController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;


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

    class CommentOutputInfo {
        public static final int maxLevel = 6;
        private int level;
        private Comment comment;

        public CommentOutputInfo(Comment comment, int level) {
            this.comment = comment;
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Comment getComment() {
            return comment;
        }

        public void setComment(Comment comment) {
            this.comment = comment;
        }
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
        result.add(new CommentOutputInfo(comment, level));

        for(Comment child : children) {
            commentsDFS(child, Math.min(level + 1, CommentOutputInfo.maxLevel), graph, result);
        }
    }

    @GetMapping("/article/{name}")
    public String home(@PathVariable(value = "name") String name, Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

        Long id = getArticleId(name);

        model.addAttribute("name", name);
        model.addAttribute("id", id);

        ArrayList<Comment> comments = commentRepository.getCommentsByArticleId(id);
        HashMap<Long, ArrayList<Comment>> graph = new HashMap<>();
        ArrayList<Comment> roots = new ArrayList<>();
        for(Comment comment : comments) {
            Long parId = comment.getParent_id();
            if(parId != null) {
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

        return "article";
    }

    @GetMapping("/test/{name}")
    public String test(@PathVariable(value = "name") String name, Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

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

    private Long getArticleId(String articleName) {
        Iterable<Long> ids = articleRepository.findIdByAddressName(articleName);
        Iterator<Long> idsItrator = ids.iterator();
        if(!idsItrator.hasNext()) {
            //TODO ERROR
        }
        return idsItrator.next();
    }
}
