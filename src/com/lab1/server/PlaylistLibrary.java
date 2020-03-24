package com.lab1.server;

import com.lab1.common.User;
import com.lab1.exception.PlaylistNotFoundException;
import com.lab1.exception.TrackNotFoundException;
import com.lab1.exception.UserNotFoundException;
import com.lab1.interfaces.PlaylistManagerRemote;
import com.lab1.common.Playlist;
import com.lab1.common.Track;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlaylistLibrary implements PlaylistManagerRemote {
    private Map<Integer, Playlist> playlists = new HashMap<>();
    int maxId = 0;
    private final MusicLibrary tracks;
    private final UserLibrary users;

    public PlaylistLibrary(MusicLibrary tracks, UserLibrary users) {
        this.tracks = tracks;
        this.users = users;
    }

    public Playlist create(int userId, String name) throws UserNotFoundException {
        Playlist playlist = new Playlist(name, this.maxId);
        this.users.get(userId).getPlaylistIds().add(this.maxId);
        this.playlists.put(this.maxId, playlist);
        this.maxId += 1;

        return playlist;
    }

    public Playlist get(int userId, int playlistId) throws PlaylistNotFoundException, UserNotFoundException {
        if (!this.playlists.containsKey(playlistId)) {
            throw new PlaylistNotFoundException("Playlist " + playlistId + " not found");
        }
        if (!this.users.get(userId).getPlaylistIds().contains(playlistId)) {
            throw new PlaylistNotFoundException("Playlist " + playlistId + " not found in user "+userId);
        }
        return this.playlists.get(playlistId);
    }

    public Map<Integer, String> getAll(int userId) throws UserNotFoundException {
        Map<Integer, String> playlists = new HashMap<>();

        for (int id : this.users.get(userId).getPlaylistIds()) {
            playlists.put(id, this.playlists.get(id).getName());
        }

        return playlists;
    }

    public void addTrack(int userId, int playlistId, Track track) throws PlaylistNotFoundException, UserNotFoundException {
        this.get(userId, playlistId).getTrackIds().add(track.getId());
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
    }

    public void removeTrack(int userId, int playlistId, int index) throws PlaylistNotFoundException, UserNotFoundException {
        this.get(userId, playlistId).getTrackIds().remove(index);
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
    }
}
