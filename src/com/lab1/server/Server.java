package com.lab1.server;

import com.lab1.interfaces.PlaylistManagerRemote;
import com.lab1.interfaces.TrackManagerRemote;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server{
    private final MusicLibrary musicLibrary;
    private final PlaylistLibrary playlistLibrary;
    static private final int SERVER_PORT = 80;

    public Server() {
        this.musicLibrary = new MusicLibrary();
        this.playlistLibrary = new PlaylistLibrary(this.musicLibrary);
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            //Создаем заглушку объекта для отправки клиенту
            TrackManagerRemote stubMusicLibrary = (TrackManagerRemote)
                UnicastRemoteObject.exportObject(server.musicLibrary, 0);
            PlaylistManagerRemote stubPlaylistLibrary = (PlaylistManagerRemote)
                UnicastRemoteObject.exportObject(server.playlistLibrary, 0);

            //Регистрируем объект
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            //Связываем объект с индификатором
            registry.bind("tracksLibrary", stubMusicLibrary);
            registry.bind("playlistsLibrary", stubPlaylistLibrary);

            System.out.println("Server start on port " + SERVER_PORT);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}