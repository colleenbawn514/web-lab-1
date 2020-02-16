package com.lab1;

import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server implements MusicLibrary {

    public Server() {}

    private ArrayList<Track> tracks = new ArrayList<>();
    private ArrayList<Playlist> playlists = new ArrayList<>();

    static private final int SERVER_PORT = 80;

    public static void main(String[] args) {
        Server server = new Server();

        try {
            //Создаем заглушку объекта для отправки клиенту
            MusicLibrary stub = (MusicLibrary) UnicastRemoteObject.exportObject(server, 0);

            //Регистрируем объект
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            //Связываем объект с индификатором
            registry.bind("music", stub);

            System.err.println("Server start on port "+SERVER_PORT);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void addTrack(String name, double size, int duration) {
        tracks.add(new Track(name, size, duration, tracks.size() + 1));
    }

    public String getTrackNameById(int trackId) {
        return tracks.get(trackId).getName();
    }

    public int getTrackDurationById(int trackId) {
        return tracks.get(trackId).getDuration();
    }

    public double getTrackSizeById(int trackId) {
        return tracks.get(trackId).getSize();
    }

    public int createPlaylist(String name) {
        playlists.add(new Playlist(name, playlists.size() + 1));

        return playlists.size();
    }

    public int[] getPlaylistsIds() {
        int[] ids = new int[playlists.size()];

        for(int i = 0; i < ids.length; i++) {
            ids[i] = playlists.get(i).getId();
        }

        return ids;
    }

    public void addTrackToPlaylist(int playlistId, int trackId) {
        playlists.get(playlistId).addTrack(trackId);
    }

    public String getPlaylistNameById(int playlistId) {
        return playlists.get(playlistId).getName();
    }

    public int[] getPlaylistById(int playlistId) {
        return playlists.get(playlistId).getTracks();
    }
}