package com.lab1;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Server implements MusicLibrary {
    private Map<Integer, Playlist> playlists = new HashMap<>();
    private int playlistIdIterator = 0;
    static private final int SERVER_PORT = 80;

    public Server() {
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            //Создаем заглушку объекта для отправки клиенту
            MusicLibrary stub = (MusicLibrary) UnicastRemoteObject.exportObject(server, 0);

            //Регистрируем объект
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            //Связываем объект с индификатором
            registry.bind("playlists", stub);

            System.err.println("Server start on port " + SERVER_PORT);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public int createPlaylist(String name) {
        int id = ++playlistIdIterator;
        this.playlists.put(id, new Playlist(name, id));

        return id;
    }

    public int createPlaylistFromFile(String name, String path) {
        int playlistId = createPlaylist(name);

        //TODO Read tracks from file

        return playlistId;
    }

    public int addTrack(int playlistId, String name, double size, int duration) {
        checkExistPlaylist(playlistId);

        return this.playlists.get(playlistId).addTrack(new Track(name, size, duration));
    }

    public void removeTrack(int playlistId, int trackId) {
        checkExistPlaylist(playlistId);

        this.playlists.get(playlistId).removeTrack(trackId);
    }

    public String getTrackNameById(int playlistId, int trackId) {
        checkExistPlaylist(playlistId);

        return this.playlists.get(playlistId).getTrack(trackId).getName();
    }

    public double getTrackSizeById(int playlistId, int trackId) {
        checkExistPlaylist(playlistId);

        return this.playlists.get(playlistId).getTrack(trackId).getSize();
    }

    public int getTrackDurationById(int playlistId, int trackId) {
        checkExistPlaylist(playlistId);

        return this.playlists.get(playlistId).getTrack(trackId).getDuration();
    }

    public int[] getPlaylistIds() {
        return this.playlists.keySet().stream().mapToInt(Number::intValue).toArray();
    }

    public String getPlaylistNameById(int playlistId) {
        checkExistPlaylist(playlistId);

        return this.playlists.get(playlistId).getName();
    }

    public int[] getPlaylistTrackIdsById(int playlistId) {
        checkExistPlaylist(playlistId);

        return this.playlists.get(playlistId).getTrackIds();
    }

    public void sortPlaylist(int playlistId, boolean isAsc) {
        checkExistPlaylist(playlistId);

        this.playlists.get(playlistId).sort(isAsc);
    }

    private void checkExistPlaylist(int playlistId) {
        if (!this.playlists.containsKey(playlistId)) {
            throw new NullPointerException("Playlist with such ID does not exist");
        }
    }
}