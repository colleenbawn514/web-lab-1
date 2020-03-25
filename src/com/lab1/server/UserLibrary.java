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
    private int maxId = 1;
    private final DB db;

    public UserLibrary(DB db) {
        this.db = db;
        try {
            this.db.execute(
                    "CREATE TABLE IF NOT EXISTS `users` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   login VARCHAR(20), " +
                            "   password VARCHAR(20), " +
                            "   name VARCHAR(20), " +
                            "   playlists TEXT " +
                            " )"
            );

            ResultSet result = this.db.executeQuery("SELECT * FROM `users`");
            int size = 0;

            System.out.println("Load users");
            while (result.next()) {
                int count = result.getMetaData().getColumnCount();
                for (int i=1; i<count+1; i++){
                    System.out.print(result.getString(i)+"  |  ");
                }
                System.out.println();

                ArrayList<Integer> playlistIds = new ArrayList<>();
                if (result.getString(5).length() != 0) {
                    for (String id : result.getString(5).split(",")) {
                        playlistIds.add(Integer.parseInt(id));
                    }
                }
                User user = new User(
                        Integer.parseInt(result.getString(1)),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        playlistIds
                );
                size += 1;
                this.usersByLogin.put(user.getLogin(), user);
                this.usersById.put(user.getId(), user);
            }
            result = this.db.executeQuery("SELECT MAX(id) FROM `users`");
            if (result.getString(1) != null) {
                this.maxId = Integer.parseInt(result.getString(1)) + 1;
            }
            System.out.println("Load " + size + " users to cache");
        } catch (SQLException e) {
            System.err.println("Error get tracks from db");
            e.printStackTrace();
        }
    }

    public User create(String login, String password, String name) throws IllegalArgumentException {
        User user = new User(this.maxId, login, password, name);
        this.usersById.put(this.maxId, user);
        this.usersByLogin.put(user.getLogin(), user);
        try {
            this.db.execute(
                    "INSERT INTO `users` " +
                            "    (login, password, name, playlists) " +
                            "VALUES " +
                            "    ('" + login + "', '" + password + "', '" + name + "', '');"
            );
        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }
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

    public void addPlaylist(int userId, int playlistId) throws UserNotFoundException {
        this.get(userId).getPlaylistIds().add(playlistId);
        updateDB(userId);
    }

    public Map<Integer, User> getAll() {
        return this.usersById;
    }

    private void updateDB(Integer userId) throws UserNotFoundException {
        ArrayList<Integer> playlistIds = this.get(userId).getPlaylistIds();
        try {
            StringBuilder playlistsBuilder = new StringBuilder();
            for (int i = 0; i < playlistIds.size(); i++) {
                playlistsBuilder.append(playlistIds.get(i));
                if (i < playlistIds.size() - 1) playlistsBuilder.append(",");
            }
            this.db.execute(
                    "UPDATE `users` " +
                            "SET " +
                            "   playlists = '" + playlistsBuilder + "' " +
                            "WHERE " +
                            "   id = " + userId
            );
        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }
    }
}
