package app.entities;

import app.exception.PlaylistNotFoundException;
import app.exception.UserNotFoundException;

import app.common.Playlist;
import app.common.Track;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistLibrary {
    private static Map<Integer, Playlist> playlists = new HashMap<>();
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
                    "CREATE TABLE IF NOT EXISTS `playlists` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   name VARCHAR(20) " +
                            " )"
            );
            DB.execute(
                    "CREATE TABLE IF NOT EXISTS `playlistTracks` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   trackId INTEGER, " +
                            "   playlistId INTEGER " +
                            " )"
            );
        } catch (SQLException e) {
            System.err.println("Error get tracks from DB");
            e.printStackTrace();
        }
    }

    public static Playlist create(int userId, String name) throws UserNotFoundException {
        int playlistId;
        Playlist playlist = null;
        try {
            DB.execute(
                    "INSERT INTO `playlists` " +
                            "(name) " +
                            "VALUES " +
                            "    ('" + name + "')"
            );
            ResultSet result = DB.executeQuery("SELECT MAX(id) FROM `playlists`");
            if (result.next()) {
                playlistId = result.getInt(1);
            } else {
                playlistId = 1;
            }
            playlist = new Playlist(name, playlistId);
            UserLibrary.addPlaylist(userId, playlistId);
            PlaylistLibrary.playlists.put(playlistId, playlist);

        } catch (SQLException e) {
            System.err.println("Error write do DB");
            e.printStackTrace();
        }

        return playlist;
    }

    public static Playlist get(int userId, int playlistId) throws PlaylistNotFoundException, UserNotFoundException {
        boolean playlistIsExist = PlaylistLibrary.isExistUserPlaylist(userId, playlistId);

        if (!playlistIsExist) {
            System.out.println("Playlist " + playlistId + " not found in DB");
            throw new PlaylistNotFoundException("Playlist " + playlistId + " not found");
        }

        boolean playlistIsCached = PlaylistLibrary.playlists.containsKey(playlistId);
        if (!playlistIsCached) {
            try {
                ResultSet result = DB.executeQuery("SELECT * FROM `playlists` WHERE `id`='" + playlistId + "'");
                if (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");

                    ResultSet resultPlaylist = DB.executeQuery("SELECT `trackId` FROM `playlistTracks` where" +
                            " `playlistId`='" + playlistId + "'");

                    ArrayList<Integer> trackIds = new ArrayList<>();
                    while (resultPlaylist.next()) {
                        trackIds.add(resultPlaylist.getInt("trackId"));
                    }

                    Playlist playlist = new Playlist(
                            name,
                            id,
                            trackIds
                    );
                    PlaylistLibrary.playlists.put(playlistId, playlist);
                    playlistIsCached = true;

                } else {
                    playlistIsCached = false;
                }

            } catch (SQLException e) {
                System.err.println("Error get playlist from DB");
                e.printStackTrace();
            }
        }
        if (!playlistIsCached) {
            throw new PlaylistNotFoundException("Playlist ID:" + playlistId + " not found");
        }

        return PlaylistLibrary.playlists.get(playlistId);
    }

    public static Map<Integer, String> getAll(int userId) throws UserNotFoundException {
        Map<Integer, String> playlists = new HashMap<>();

        try {
            ResultSet result = DB.executeQuery("SELECT * FROM `playlists` WHERE `id` IN " +
                    "(SELECT `playlistId` FROM `usersPlaylists` WHERE `userId`='" + userId + "')");

            while (result.next()) {
                playlists.put(result.getInt("id"), result.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error get MusicLibrary from DB");
            e.printStackTrace();
        }

        return playlists;
    }

    public static void addTrack(int userId, int playlistId, Track track) throws PlaylistNotFoundException, UserNotFoundException {

        System.out.println("Check exist in cache " + PlaylistLibrary.get(userId, playlistId).getTrackIds().contains(track.getId()));
        if (!PlaylistLibrary.get(userId, playlistId).getTrackIds().contains(track.getId())) {
            PlaylistLibrary.get(userId, playlistId).getTrackIds().add(track.getId());
            try {
                DB.execute(
                        "INSERT INTO  `playlistTracks` " +
                                "    ( trackId, playlistId) " +
                                "VALUES " +
                                "    ('" + track.getId() + "', '" + playlistId + "');"
                );
                System.out.println("Add track " + track.getId() + " for playlist " + playlistId + " for user " + userId);
            } catch (SQLException e) {
                System.err.println("Error write do DB");
                e.printStackTrace();
            }
        }
    }

    public static void sort(int userId, int playlistId, boolean isAsc) throws PlaylistNotFoundException, UserNotFoundException {
         System.out.println(isAsc);
         Playlist playlist = PlaylistLibrary.get(userId, playlistId);
        try {
            ResultSet result = DB.executeQuery(
                    "SELECT `tracks`.`id` FROM `playlistTracks`, `tracks`, `authors`" +
                            "WHERE `playlistTracks`.`playlistId`='" + playlistId + "'"   +
                            "AND `playlistTracks`.`trackId`=`tracks`.`id`"+
                            " AND `tracks`.`author`=`authors`.`id` " +
                            "ORDER BY  `authors`.`name` "+ (isAsc ? "ASC" : "DESC") +", `tracks`.`name` " + (isAsc ? "ASC" : "DESC")
            );

            System.out.println("Sort tracks in playlist " + playlistId + " for user " + userId);
            playlist.getTrackIds().clear();
            while (result.next()) {
                playlist.getTrackIds().add(result.getInt("id"));
            }
        } catch (SQLException e) {
            System.err.println("Error write do DB");
            e.printStackTrace();
        }

    }

    public static void removeTrack(int userId, int playlistId, Track track) throws PlaylistNotFoundException, UserNotFoundException {
        PlaylistLibrary.get(userId, playlistId).getTrackIds().remove(track.getId());
        try {
            DB.execute(
                    "DELETE FROM `playlistTracks` " +
                            "    WHERE `trackId`='" + track.getId() + "' AND `playlistId`=' " + playlistId + "'"
            );
            System.out.println("Delete track " + track.getId() + " in playlist " + playlistId + " for user " + userId);
        } catch (SQLException e) {
            System.err.println("Error write do DB");
            e.printStackTrace();
        }
    }

    public static boolean isExistPlaylist(int playlistId) throws PlaylistNotFoundException {
        boolean playlistIsExist = PlaylistLibrary.playlists.containsKey(playlistId);
        if (!playlistIsExist) {
            System.out.println("Playlist " + playlistId + " not find in cache. Search in DB");
            try {
                ResultSet result = DB.executeQuery("SELECT id FROM `playlists` WHERE `id`='" + playlistId + "'");
                playlistIsExist = result.next();
                System.out.println("Playlist " + playlistId + " in DB: " + (playlistIsExist ? "found" : "not found"));
            } catch (SQLException e) {
                System.err.println("Error get tracks from DB");
                e.printStackTrace();
            }
        }
        return playlistIsExist;
    }

    public static boolean isExistUserPlaylist(int userId, int playlistId) throws PlaylistNotFoundException, UserNotFoundException {
        boolean playlistIsExist = PlaylistLibrary.playlists.containsKey(playlistId);
        if (!playlistIsExist) {
            System.out.println("Playlist " + playlistId + " not find in cache. Search in DB");
            try {
                ResultSet result = DB.executeQuery("SELECT id FROM `usersPlaylists` WHERE " +
                        "`userId`='" + userId + "' AND `playlistId`='" + playlistId + "'");
                playlistIsExist = result.next();
                System.out.println("Playlist " + playlistId + " in DB: " + (playlistIsExist ? "found" : "not found"));
            } catch (SQLException e) {
                System.err.println("Error get tracks from DB");
                e.printStackTrace();
            }
        } else {
            playlistIsExist = UserLibrary.get(userId).getPlaylistIds().contains(playlistId);
        }
        return playlistIsExist;
    }

}
