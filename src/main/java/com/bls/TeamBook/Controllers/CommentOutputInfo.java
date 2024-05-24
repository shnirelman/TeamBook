package com.bls.TeamBook.Controllers;

import com.bls.TeamBook.models.Comment;
import com.bls.TeamBook.models.MyUser;
import com.bls.TeamBook.repo.UserRepository;

import java.util.Optional;

public class CommentOutputInfo {
    public static final int maxLevel = 6;
    private int level;
    private Comment comment;

    private String userName;

    public CommentOutputInfo(UserRepository userRepository, Comment comment, int level) {
        this.comment = comment;
        this.level = level;
        Optional<MyUser> author = userRepository.findById(comment.getUser_id());
        author.ifPresent(myUser -> userName = myUser.getLogin());
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
