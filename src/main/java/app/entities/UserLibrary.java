package app.entities;
import app.entities.DB;
import app.common.User;
import app.exception.UserNotFoundException;

import javax.naming.AuthenticationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserLibrary {
    private static Map<Integer, User> usersById = new HashMap<>();
    private static Map<String, User> usersByLogin = new HashMap<>();
    static {
        try {
            UserLibrary.connectToDB();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void connectToDB() throws SQLException, ClassNotFoundException {
        DB.connection();
        try {
            DB.execute(
                    "CREATE TABLE IF NOT EXISTS `users` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   login VARCHAR(20), " +
                            "   password VARCHAR(20), " +
                            "   name VARCHAR(20) " +
                            " )"
            );
            DB.execute(
                    "CREATE TABLE IF NOT EXISTS `usersPlaylists` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   playlistId INTEGER, " +
                            "   userId INTEGER " +
                            " )"
            );
        } catch (SQLException e) {
            System.err.println("Error get tracks from DB");
            e.printStackTrace();
        }
    }

    public static User create(String login, String password, String name) throws IllegalArgumentException {
        int userId;
        User user = null;
        try {
            DB.execute(
                    "INSERT INTO `users` " +
                            "    (login, password, name) " +
                            "VALUES " +
                            "    ('" + login + "', '" + password + "', '" + name + "');"
            );
            ResultSet result = DB.executeQuery("SELECT MAX(id) FROM `users`");
            if(result.next()){
                userId = result.getInt(1);
            } else {
                userId = 1;
            }
            user = new User(userId, login, password, name);
            UserLibrary.usersById.put(userId, user);
            UserLibrary.usersByLogin.put(user.getLogin(), user);
        } catch (SQLException e) {
            System.err.println("Error write do DB");
            e.printStackTrace();
        }

        return user;
    }

    public static User get(int userId) throws UserNotFoundException {
        boolean userIsCached = UserLibrary.usersById.containsKey(userId);
        if (!userIsCached) {
            try {
                ResultSet result = DB.executeQuery("SELECT * FROM `users` where `id`='" + userId+"'");
                if (result.next()) {

                    int id = result.getInt("id");
                    String login = result.getString("login");
                    String password = result.getString("pasword");
                    String name = result.getString("name");

                    ResultSet resultPlaylist = DB.executeQuery("SELECT * FROM `usersPlaylists` where `userId`='" + result.getString("id") + "'");

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
                    UserLibrary.usersByLogin.put(user.getLogin(), user);
                    UserLibrary.usersById.put(user.getId(), user);
                    userIsCached = true;

                } else {
                    userIsCached = false;
                }

            } catch (SQLException e) {
                System.err.println("Error get tracks from DB");
                e.printStackTrace();
            }
        }
        if (!userIsCached) {
            throw new UserNotFoundException("User ID:" + userId + " not found");
        }
        return UserLibrary.usersById.get(userId);
    }

    public static User get(String login, String password) throws UserNotFoundException, AuthenticationException {
        boolean userIsExist = UserLibrary.isExistUser(login);
        boolean passwordIsEquals = false;

        if (!userIsExist) {
            System.out.println("User " + login + " not found in DB");
            throw new UserNotFoundException("User " + login + " not found");
        }
        if (!UserLibrary.usersByLogin.containsKey(login)) {
            try {
                ResultSet result = DB.executeQuery("SELECT * FROM `users` where `login`='" + login + "' and `password`='" + password+"'");
                if (result.next()) {

                    int id = result.getInt("id");
                    String name = result.getString("name");

                    ResultSet resultPlaylist = DB.executeQuery("SELECT * FROM `usersPlaylists` where `userId`='" + result.getString("id") + "'");

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
                    UserLibrary.usersByLogin.put(user.getLogin(), user);
                    UserLibrary.usersById.put(user.getId(), user);
                    passwordIsEquals = true;
                    System.out.println("User " + login + " is cached.");
                }
            } catch (SQLException e) {
                System.err.println("Error get tracks from DB");
                e.printStackTrace();
            }
        }else {
            passwordIsEquals = UserLibrary.usersByLogin.get(login).equalsPassword(password);
        }
        if (!passwordIsEquals) {
            throw new AuthenticationException("Error password for user " + login);
        }

        return UserLibrary.usersByLogin.get(login);
    }

    public static boolean isExistUser(String login) {
        boolean userIsExist = UserLibrary.usersByLogin.containsKey(login);
        if (!userIsExist) {
            System.out.println("User " + login + " not find in cache. Search in DB");
            try {
                ResultSet result = DB.executeQuery("SELECT login FROM `users` WHERE `login`='" + login + "'");
                userIsExist = result.next();
                System.out.println("User " + login + " in DB: " + (userIsExist ? "found" : "not found"));
            } catch (SQLException e) {
                System.err.println("Error get tracks from DB");
                e.printStackTrace();
            }
        }
        return userIsExist;
    }

    public static void addPlaylist(int userId, int playlistId) throws UserNotFoundException {
        UserLibrary.get(userId).getPlaylistIds().add(playlistId);
        try {
            DB.execute(
                    "INSERT INTO  `usersPlaylists` " +
                            "    (playlistId, userId) " +
                            "VALUES " +
                            "    ('" + playlistId + "', '" + userId + "');"
            );
            System.out.println("Add playlist " + playlistId + " for user " + UserLibrary.get(userId).getLogin());
        } catch (SQLException e) {
            System.err.println("Error write do DB");
            e.printStackTrace();
        }
    }

    public Map<Integer, User> getAll() {
        return UserLibrary.usersById;
    }
}
