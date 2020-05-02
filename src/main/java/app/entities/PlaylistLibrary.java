package app.entities;

import app.exception.PlaylistNotFoundException;
import app.exception.UserNotFoundException;
import app.interfaces.PlaylistManagerRemote;
import app.common.Playlist;
import app.common.Track;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistLibrary implements PlaylistManagerRemote {
    private Map<Integer, Playlist> playlists = new HashMap<>();
    private final MusicLibrary tracks;
    private final UserLibrary users;
    private final DB db;

    public PlaylistLibrary(MusicLibrary tracks, UserLibrary users, DB db) {
        this.tracks = tracks;
        this.users = users;
        this.db = db;
        try {
            this.db.execute(
                    "CREATE TABLE IF NOT EXISTS `playlists` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   name VARCHAR(20) " +
                            " )"
            );
            this.db.execute(
                    "CREATE TABLE IF NOT EXISTS `playlistTracks` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   trackId INTEGER, " +
                            "   playlistId INTEGER " +
                            " )"
            );
        } catch (SQLException e) {
            System.err.println("Error get tracks from db");
            e.printStackTrace();
        }
    }

    public Playlist create(int userId, String name) throws UserNotFoundException {
        int playlistId;
        Playlist playlist = null;
        try {
            this.db.execute(
                    "INSERT INTO `playlists` " +
                            "(name) " +
                            "VALUES " +
                            "    ('" + name + "')"
            );
            ResultSet result = this.db.executeQuery("SELECT MAX(id) FROM `playlists`");
            if (result.next()) {
                playlistId = result.getInt(1);
            } else {
                playlistId = 1;
            }
            playlist = new Playlist(name, playlistId);
            this.users.addPlaylist(userId, playlistId);
            this.playlists.put(playlistId, playlist);

        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }

        return playlist;
    }

    public Playlist get(int userId, int playlistId) throws PlaylistNotFoundException, UserNotFoundException {
        boolean playlistIsExist = this.isExistUserPlaylist(userId, playlistId);

        if (!playlistIsExist) {
            System.out.println("Playlist " + playlistId + " not found in DB");
            throw new PlaylistNotFoundException("Playlist " + playlistId + " not found");
        }

        boolean playlistIsCached = this.playlists.containsKey(playlistId);
        if (!playlistIsCached) {
            try {
                ResultSet result = this.db.executeQuery("SELECT * FROM `playlists` WHERE `id`='" + playlistId + "'");
                if (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");

                    ResultSet resultPlaylist = this.db.executeQuery("SELECT `trackId` FROM `playlistTracks` where" +
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
                    this.playlists.put(playlistId, playlist);
                    playlistIsCached = true;

                } else {
                    playlistIsCached = false;
                }

            } catch (SQLException e) {
                System.err.println("Error get playlist from db");
                e.printStackTrace();
            }
        }
        if (!playlistIsCached) {
            throw new PlaylistNotFoundException("Playlist ID:" + playlistId + " not found");
        }

        return this.playlists.get(playlistId);
    }

    public Map<Integer, String> getAll(int userId) throws UserNotFoundException {
        Map<Integer, String> playlists = new HashMap<>();

        try {
            ResultSet result = this.db.executeQuery("SELECT * FROM `playlists` WHERE `id` IN " +
                    "(SELECT `playlistId` FROM `usersPlaylists` WHERE `userId`='" + userId + "')");
            while (result.next()) {
                playlists.put(result.getInt("id"), result.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error get tracks from db");
            e.printStackTrace();
        }

        return playlists;
    }

    public void addTrack(int userId, int playlistId, Track track) throws PlaylistNotFoundException, UserNotFoundException {

        System.out.println("Check exist in cache " + this.get(userId, playlistId).getTrackIds().contains(track.getId()));
        if (!this.get(userId, playlistId).getTrackIds().contains(track.getId())) {
            this.get(userId, playlistId).getTrackIds().add(track.getId());
            try {
                this.db.execute(
                        "INSERT INTO  `playlistTracks` " +
                                "    ( trackId, playlistId) " +
                                "VALUES " +
                                "    ('" + track.getId() + "', '" + playlistId + "');"
                );
                System.out.println("Add track " + track.getId() + " for playlist " + playlistId + " for user " + userId);
            } catch (SQLException e) {
                System.err.println("Error write do db");
                e.printStackTrace();
            }
        }
    }

    public void sort(int userId, int playlistId, boolean isAsc) throws PlaylistNotFoundException, UserNotFoundException {
         System.out.println(isAsc);
         Playlist playlist = this.get(userId, playlistId);
        try {
            ResultSet result = this.db.executeQuery(
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
            System.err.println("Error write do db");
            e.printStackTrace();
        }

    }

    public void removeTrack(int userId, int playlistId, Track track) throws PlaylistNotFoundException, UserNotFoundException {
        this.get(userId, playlistId).getTrackIds().remove(track.getId());
        try {
            this.db.execute(
                    "DELETE FROM `playlistTracks` " +
                            "    WHERE `trackId`='" + track.getId() + "' AND `playlistId`=' " + playlistId + "'"
            );
            System.out.println("Delete track " + track.getId() + " in playlist " + playlistId + " for user " + userId);
        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }
    }

   /* public void removeDuplicate(int userId, int playlistId) throws PlaylistNotFoundException, TrackNotFoundException, UserNotFoundException {
        ArrayList<Integer> tracks = this.get(userId, playlistId).getTrackIds();
        for (int i = 0; i < this.get(userId, playlistId).getSize(); i++) {
            for (int j = i + 1; j < this.get(userId, playlistId).getSize(); j++) {
                if (tracks.get(i).equals(tracks.get(j))) {
                    System.out.println("Remove " + tracks.get(i) + " name " + this.tracks.get(tracks.get(i)).getName());
                    this.removeTrack(userId, playlistId, j);
                    j--;
                }
            }
        }
        updateDB(userId, playlistId);
    }*/

    public boolean isExistPlaylist(int playlistId) throws PlaylistNotFoundException {
        boolean playlistIsExist = this.playlists.containsKey(playlistId);
        if (!playlistIsExist) {
            System.out.println("Playlist " + playlistId + " not find in cache. Search in DB");
            try {
                ResultSet result = this.db.executeQuery("SELECT id FROM `playlists` WHERE `id`='" + playlistId + "'");
                playlistIsExist = result.next();
                System.out.println("Playlist " + playlistId + " in DB: " + (playlistIsExist ? "found" : "not found"));
            } catch (SQLException e) {
                System.err.println("Error get tracks from db");
                e.printStackTrace();
            }
        }
        return playlistIsExist;
    }

    public boolean isExistUserPlaylist(int userId, int playlistId) throws PlaylistNotFoundException, UserNotFoundException {
        boolean playlistIsExist = this.playlists.containsKey(playlistId);
        if (!playlistIsExist) {
            System.out.println("Playlist " + playlistId + " not find in cache. Search in DB");
            try {
                ResultSet result = this.db.executeQuery("SELECT id FROM `usersPlaylists` WHERE " +
                        "`userId`='" + userId + "' AND `playlistId`='" + playlistId + "'");
                playlistIsExist = result.next();
                System.out.println("Playlist " + playlistId + " in DB: " + (playlistIsExist ? "found" : "not found"));
            } catch (SQLException e) {
                System.err.println("Error get tracks from db");
                e.printStackTrace();
            }
        } else {
            playlistIsExist = this.users.get(userId).getPlaylistIds().contains(playlistId);
        }
        return playlistIsExist;
    }

}
