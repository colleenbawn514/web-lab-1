package com.colleen.letdown.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @NotNull(message = "login can't be null")
    @NotEmpty(message = "login can't be empty")
    @Size(min = 2, max = 100, message = "Login must be between 2 and 100 characters")
    @Column(name = "login", length = 64, nullable = false, unique = true)
    private String login;

    @NotNull(message = "password can't be null")
    @NotEmpty(message = "password can't be empty")
    @Size(min = 5, max = 255, message = "Password must be between 5 and 255 characters")
    @Column(name = "password", length = 128, nullable = false)
    private String password;

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString(){
        return String.format("User[id=%s, login=%s, password=%s]", id, login, password);
    }
}
