package com.lab1.server;

import com.lab1.exception.PlaylistNotFoundException;
import com.lab1.exception.TrackNotFoundException;
import com.lab1.exception.UserNotFoundException;
import com.lab1.interfaces.PlaylistManagerRemote;
import com.lab1.common.Playlist;
import com.lab1.common.Track;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlaylistLibrary implements PlaylistManagerRemote {
    private Map<Integer, Playlist> playlists = new HashMap<>();
    int maxId = 1;
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
                            "   name VARCHAR(20), " +
                            "   tracks TEXT " +
                            " )"
            );

            ResultSet result = this.db.executeQuery("SELECT * FROM `playlists`");
            int size = 0;

            System.out.println("load playlists");
            while (result.next()) {
                int count = result.getMetaData().getColumnCount();
                for (int i = 1; i < count + 1; i++) {
                    System.out.print(result.getString(i) + "  |  ");
                }
                System.out.println();

                ArrayList<Integer> trackIds = new ArrayList<>();
                if (result.getString(3).length() != 0) {
                    for (String id : result.getString(3).split(",")) {
                        trackIds.add(Integer.parseInt(id));
                    }
                }
                Playlist playlist = new Playlist(
                        result.getString(2),
                        Integer.parseInt(result.getString(1)),
                        trackIds
                );
                size += 1;
                this.playlists.put(Integer.parseInt(result.getString(1)), playlist);
            }
            result = this.db.executeQuery("SELECT MAX(id) FROM `playlists`");
            if (result.getString(1) != null) {
                this.maxId = Integer.parseInt(result.getString(1)) + 1;
            }
            System.out.println("Load " + size + " playlists to cache");
        } catch (SQLException e) {
            System.err.println("Error get tracks from db");
            e.printStackTrace();
        }
    }

    public Playlist create(int userId, String name) throws UserNotFoundException {
        Playlist playlist = new Playlist(name, this.maxId);
        this.users.addPlaylist(userId, this.maxId);
        this.playlists.put(this.maxId, playlist);
        try {
            this.db.execute(
                    "INSERT INTO `playlists` " +
                            "    (name, tracks) " +
                            "VALUES " +
                            "    ('" + name + "', '');"
            );
        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }
        this.maxId += 1;

        return playlist;
    }

    public Playlist get(int userId, int playlistId) throws PlaylistNotFoundException, UserNotFoundException {
        if (!this.playlists.containsKey(playlistId)) {
            throw new PlaylistNotFoundException("Playlist " + playlistId + " not found");
        }
        if (!this.users.get(userId).getPlaylistIds().contains(playlistId)) {
            throw new PlaylistNotFoundException("Playlist " + playlistId + " not found in user " + userId);
        }
        return this.playlists.get(playlistId);
    }

    public Map<Integer, String> getAll(int userId) throws UserNotFoundException {
        Map<Integer, String> playlists = new HashMap<>();

        for (int id : this.users.get(userId).getPlaylistIds()) {
            System.out.println(id);
            playlists.put(id, this.playlists.get(id).getName());
        }

        return playlists;
    }

    public void addTrack(int userId, int playlistId, Track track) throws PlaylistNotFoundException, UserNotFoundException {
        this.get(userId, playlistId).getTrackIds().add(track.getId());
        updateDB(userId, playlistId);
    }

    public void sort(int userId, int playlistId, boolean isAsc) throws PlaylistNotFoundException, UserNotFoundException {
        Playlist playlist = this.get(userId, playlistId);
        Comparator<Integer> comparator = Comparator.comparing(id -> {
            try {
                return tracks.get(id).getArtist() + tracks.get(id).getName();
            } catch (TrackNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        });

        if (isAsc) {
            playlist.getTrackIds().sort(comparator);
        } else {
            playlist.getTrackIds().sort(comparator.reversed());
        }
        updateDB(userId, playlistId);
    }

    public void removeTrack(int userId, int playlistId, int index) throws PlaylistNotFoundException, UserNotFoundException {
        this.get(userId, playlistId).getTrackIds().remove(index);
        updateDB(userId, playlistId);
    }

    public void removeDuplicate(int userId, int playlistId) throws PlaylistNotFoundException, TrackNotFoundException, UserNotFoundException {
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
    }

    private void updateDB(Integer userId, Integer playlistId) throws UserNotFoundException, PlaylistNotFoundException {
        ArrayList<Integer> tracks = this.get(userId, playlistId).getTrackIds();
        try {
            StringBuilder tracksBuilder = new StringBuilder();
            for (int i = 0; i < tracks.size(); i++) {
                tracksBuilder.append(tracks.get(i));
                if (i < tracks.size() - 1) tracksBuilder.append(",");
            }
            this.db.execute(
                    "UPDATE `playlists` " +
                            "SET " +
                            "   tracks = '" + tracksBuilder + "' " +
                            "WHERE " +
                            "   id = " + playlistId
            );
        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }
    }
}
