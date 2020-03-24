package com.lab1.server;

import com.lab1.common.User;
import com.lab1.exception.UserNotFoundException;
import com.lab1.interfaces.UserManagerRemote;

import javax.naming.AuthenticationException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class UserLibrary implements UserManagerRemote {
    private Map<Integer, User> usersById = new HashMap<>();
    private Map<String, User> usersByLogin = new HashMap<>();
    private int maxId = 0;

    public User create(String login, String password, String name) throws IllegalArgumentException {
        User user = new User(this.maxId, login, password, name);
        this.usersById.put(this.maxId, user);
        this.usersByLogin.put(user.getLogin(), user);
        this.maxId += 1;

        return user;
    }

    public User get(int userId) throws UserNotFoundException {
        if (!this.usersById.containsKey(userId)) {
            throw new UserNotFoundException("User ID:" + userId + " not found");
        }
        return this.usersById.get(userId);
    }

    public User get(String login, String password) throws UserNotFoundException, AuthenticationException {
        if (!this.usersByLogin.containsKey(login)) {
            throw new UserNotFoundException("User Login:" + login + " not found");
        }
        if (!this.usersByLogin.get(login).equalsPassword(password)) {
            throw new AuthenticationException("Error password for user " + login);
        }
        return this.usersByLogin.get(login);
    }

    public boolean isExistUser(String login) throws UserNotFoundException {
        return this.usersByLogin.containsKey(login);
    }

    public Map<Integer, User> getAll() {
        return this.usersById;
    }
}
