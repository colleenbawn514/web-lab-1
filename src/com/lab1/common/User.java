package com.lab1.common;

import com.lab1.interfaces.UserRMI;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable, UserRMI {
    private static final long serialVersionUID = 1;
    private int id;
    private String login;
    private String password;
    private String name;
    private ArrayList<Integer> playlistIds;

    public User(int id, String login, String password, String name) {
        this.id = id;
        if (login.trim().equals("")) {
            throw new IllegalArgumentException("The login must not be empty");
        }
        this.login = login.trim();
        if (password.trim().equals("")) {
            throw new IllegalArgumentException("The password must not be empty");
        }
        if (password.trim().length() < 5) {
            throw new IllegalArgumentException("The password length must be more 5 symbols");
        }
        this.password = password.trim();
        if (name.trim().equals("")) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        this.name = name.trim();
        this.playlistIds = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public String getLogin() {
        return this.login;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Integer> getPlaylistIds() {
        return this.playlistIds;
    }

    public boolean equalsPassword(String password) {
        return this.password.equals(password);
    }
}
