package com.bls.TeamBook.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;

    public MyUser() {}
    public MyUser(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
