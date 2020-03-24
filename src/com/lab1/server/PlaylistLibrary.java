package com.lab1.server;

import com.lab1.exception.PlaylistNotFoundException;
import com.lab1.exception.TrackNotFoundException;
import com.lab1.interfaces.PlaylistManagerRemote;
import com.lab1.common.Playlist;
import com.lab1.common.Track;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlaylistLibrary implements PlaylistManagerRemote {
    private Map<Integer, Playlist> playlists = new HashMap<>();
    private int maxId = 0;
    private final MusicLibrary tracks;

    public PlaylistLibrary(MusicLibrary tracks) {
        this.tracks = tracks;
    }

    public Playlist create(String name) {
        Playlist playlist = new Playlist(name, this.maxId);
        this.playlists.put(this.maxId, playlist);
        this.maxId += 1;

        return playlist;
    }

    public Playlist get(int playlistId) throws PlaylistNotFoundException {
        if (!this.playlists.containsKey(playlistId)) {
            throw new PlaylistNotFoundException("Playlist " + playlistId + " not found");
        }
        return this.playlists.get(playlistId);
    }

    public Map<Integer, String> getAll() {
        Map<Integer, String> playlists = new HashMap<>();

        for (int id : this.playlists.keySet()) {
            playlists.put(id, this.playlists.get(id).getName());
        }

        return playlists;
    }

    public void addTrack(int playlistId, Track track) throws PlaylistNotFoundException {
        this.get(playlistId).getTrackIds().add(track.getId());
    }

    public void sort(int playlistId, boolean isAsc) throws PlaylistNotFoundException {
        Playlist playlist = this.get(playlistId);
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

    public void removeTrack(int playlistId, int index) throws PlaylistNotFoundException {
        this.get(playlistId).getTrackIds().remove(index);
    }

    public void removeDuplicate(int playlistId) throws PlaylistNotFoundException, TrackNotFoundException {
        for (int i = 0; i < this.get(playlistId).getSize(); i++) {
            for (int j = i + 1; j < this.get(playlistId).getSize(); j++) {

                boolean isSame = this.tracks.get(i).equals(this.tracks.get(j));
                if (isSame) {
                    System.out.println("Remove " + j + " name " + this.tracks.get(j).getName());
                    this.removeTrack(playlistId, j);
                    j--;
                }
            }
        }
    }
}
