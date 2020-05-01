package com.lab1.server;

import com.lab1.common.Playlist;
import com.lab1.common.Track;
import com.lab1.common.User;
import com.lab1.exception.PlaylistNotFoundException;
import com.lab1.exception.UserNotFoundException;
import com.lab1.interfaces.UserManagerRemote;

import javax.naming.AuthenticationException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserLibrary implements UserManagerRemote {
    private Map<Integer, User> usersById = new HashMap<>();
    private Map<String, User> usersByLogin = new HashMap<>();
    private final DB db;

    public UserLibrary(DB db) {
        this.db = db;
        try {
            this.db.execute(
                    "CREATE TABLE IF NOT EXISTS `users` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   login VARCHAR(20), " +
                            "   password VARCHAR(20), " +
                            "   name VARCHAR(20) " +
                            " )"
            );
            this.db.execute(
                    "CREATE TABLE IF NOT EXISTS `usersPlaylists` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   playlistId INTEGER, " +
                            "   userId INTEGER " +
                            " )"
            );
        } catch (SQLException e) {
            System.err.println("Error get tracks from db");
            e.printStackTrace();
        }
    }

    public User create(String login, String password, String name) throws IllegalArgumentException {
        int userId;
        User user = null;
        try {
            this.db.execute(
                    "INSERT INTO `users` " +
                            "    (login, password, name) " +
                            "VALUES " +
                            "    ('" + login + "', '" + password + "', '" + name + "');"
            );
            ResultSet result = this.db.executeQuery("SELECT MAX(id) FROM `users`");
            if(result.next()){
                userId = result.getInt(1);
            } else {
                userId = 1;
            }
            user = new User(userId, login, password, name);
            this.usersById.put(userId, user);
            this.usersByLogin.put(user.getLogin(), user);
        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }

        return user;
    }

    public User get(int userId) throws UserNotFoundException {
        boolean userIsCached = this.usersById.containsKey(userId);
        if (!userIsCached) {
            try {
                ResultSet result = this.db.executeQuery("SELECT * FROM `users` where `id`='" + userId+"'");
                if (result.next()) {

                    int id = result.getInt("id");
                    String login = result.getString("login");
                    String password = result.getString("pasword");
                    String name = result.getString("name");

                    ResultSet resultPlaylist = this.db.executeQuery("SELECT * FROM `usersPlaylists` where `userId`='" + result.getString("id") + "'");

                    ArrayList<Integer> playlistIds = new ArrayList<>();
                    while (resultPlaylist.next()) {
                        playlistIds.add(resultPlaylist.getInt("playlistId"));
                    }
                    System.out.print(id + "  |  ");
                    System.out.print(login + "  |  ");
                    System.out.print(password + "  |  ");
                    System.out.println(name + "  |  ");

                    for (int playlistId : playlistIds) {
                        System.out.print(playlistId + "; ");
                    }
                    System.out.println();
                    User user = new User(
                            id,
                            login,
                            password,
                            name,
                            playlistIds
                    );
                    this.usersByLogin.put(user.getLogin(), user);
                    this.usersById.put(user.getId(), user);
                    userIsCached = true;

                } else {
                    userIsCached = false;
                }

            } catch (SQLException e) {
                System.err.println("Error get tracks from db");
                e.printStackTrace();
            }
        }
        if (!userIsCached) {
            throw new UserNotFoundException("User ID:" + userId + " not found");
        }
        return this.usersById.get(userId);
    }

    public User get(String login, String password) throws UserNotFoundException, AuthenticationException {
        boolean userIsExist = this.isExistUser(login);
        boolean passwordIsEquals = false;

        if (!userIsExist) {
            System.out.println("User " + login + " not found in DB");
            throw new UserNotFoundException("User " + login + " not found");
        }
        if (!this.usersByLogin.containsKey(login)) {
            try {
                ResultSet result = this.db.executeQuery("SELECT * FROM `users` where `login`='" + login + "' and `password`='" + password+"'");
                if (result.next()) {

                    int id = result.getInt("id");
                    String name = result.getString("name");

                    ResultSet resultPlaylist = this.db.executeQuery("SELECT * FROM `usersPlaylists` where `userId`='" + result.getString("id") + "'");

                    ArrayList<Integer> playlistIds = new ArrayList<>();
                    while (resultPlaylist.next()) {
                        playlistIds.add(resultPlaylist.getInt("playlistId"));
                    }
                    System.out.print(id + "  |  ");
                    System.out.print(login + "  |  ");
                    System.out.print(password + "  |  ");
                    System.out.print(name + "  |  ");

                    for (int playlistId : playlistIds) {
                        System.out.print(playlistId + "; ");
                    }
                    System.out.println();
                    User user = new User(
                            id,
                            login,
                            password,
                            name,
                            playlistIds
                    );
                    this.usersByLogin.put(user.getLogin(), user);
                    this.usersById.put(user.getId(), user);
                    passwordIsEquals = true;
                    System.out.println("User " + login + " is cached.");
                }
            } catch (SQLException e) {
                System.err.println("Error get tracks from db");
                e.printStackTrace();
            }
        }else {
            passwordIsEquals = this.usersByLogin.get(login).equalsPassword(password);
        }
        if (!passwordIsEquals) {
            throw new AuthenticationException("Error password for user " + login);
        }

        return this.usersByLogin.get(login);
    }

    public boolean isExistUser(String login) throws UserNotFoundException {
        boolean userIsExist = this.usersByLogin.containsKey(login);
        if (!userIsExist) {
            System.out.println("User " + login + " not find in cache. Search in DB");
            try {
                ResultSet result = this.db.executeQuery("SELECT login FROM `users` WHERE `login`='" + login + "'");
                userIsExist = result.next();
                System.out.println("User " + login + " in DB: " + (userIsExist ? "found" : "not found"));
            } catch (SQLException e) {
                System.err.println("Error get tracks from db");
                e.printStackTrace();
            }
        }
        return userIsExist;
    }

    public void addPlaylist(int userId, int playlistId) throws UserNotFoundException {
        this.get(userId).getPlaylistIds().add(playlistId);
        try {
            this.db.execute(
                    "INSERT INTO  `usersPlaylists` " +
                            "    (playlistId, userId) " +
                            "VALUES " +
                            "    ('" + playlistId + "', '" + userId + "');"
            );
            System.out.println("Add playlist " + playlistId + " for user " + this.get(userId).getLogin());
        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }
    }

    public Map<Integer, User> getAll() {
        return this.usersById;
    }
}
