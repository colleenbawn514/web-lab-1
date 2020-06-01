package com.colleen.letdown.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "token", length = 36, nullable = false)
    private String token;

    @Column(name = "user_id",  nullable = false)
    private long userId;

    public long getId() {
        return id;
    }

    public String  getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getId();
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    @OneToOne
    @PrimaryKeyJoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiration_date",  nullable = false)
    private Timestamp expirationDate;
}
