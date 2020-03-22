package com.lab1.server;

import com.lab1.interfaces.PlaylistManagerRMI;
import com.lab1.common.Playlist;
import com.lab1.common.Track;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlaylistManager implements PlaylistManagerRMI {
    private Map<Integer, Playlist> playlists = new HashMap<>();
    private int maxId = 0;
    private final MusicLibrary library;

    public PlaylistManager(MusicLibrary library){
        this.library = library;
    }

    public Playlist get(int playlistId) {
        return this.playlists.get(playlistId);
    }

    public ArrayList<Integer> getAllIds() {
        return new ArrayList<>(this.playlists.keySet());
    }

    public Map<Integer, Track> getTracks(int playlistId) {
        ArrayList<Integer> trackIds = this.playlists.get(playlistId).getTrackIds();
        Map<Integer, Track> tracks = new HashMap<>();
        for(int id : trackIds) {
            tracks.put(id, this.library.getTrack(id));
        }

        return tracks;
    }

    public Playlist create(String name) {
        this.maxId += 1;
        Playlist playlist = new Playlist(name, this.maxId, this.library);

        this.playlists.put(this.maxId, playlist);

        return playlist;
    }

    public void sort(int playlistId, boolean isAsc) {
        Playlist playlist = this.playlists.get(playlistId);
        Comparator<Integer> comparator = Comparator.comparing(id -> library.getTrack(id).getName());

        if (isAsc) {
            playlist.getTrackIds().sort(comparator);
        } else {
            playlist.getTrackIds().sort(comparator.reversed());
        }
    }

    public void addTrack(int playlistId, Track track) throws RemoteException {
        this.playlists.get(playlistId).getTrackIds().add(track.getId());
    }

    public void removeTrack(int playlistId, int index) throws RemoteException {
        this.playlists.get(playlistId).getTrackIds().remove(index);
    }

    public void removeDuplicate(int playlistId) throws RemoteException {
        for (int i = 0; i < this.playlists.get(playlistId).getCountsTracks(); i++) {
            for (int j = i + 1; j < this.playlists.get(playlistId).getCountsTracks(); j++) {

                boolean isSame = this.library.getTrack(i).equals(this.library.getTrack(j));
                if (isSame) {
                    System.out.println("Remove " + j + " name " + this.library.getTrack(j).getName());
                    this.removeTrack(playlistId, j);
                    j--;
                }
            }
        }
    }
}
