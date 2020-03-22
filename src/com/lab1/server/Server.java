package com.lab1.server;

import com.lab1.interfaces.PlaylistManagerRMI;
import com.lab1.interfaces.TrackManagerRMI;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server{
    private final MusicLibrary library;
    static private final int SERVER_PORT = 80;

    public Server() {
        this.library = new MusicLibrary();
    }

    public static void main(String[] args) {
        Server server = new Server();
        TrackManager trackRemote = new TrackManager(server.library);
        PlaylistManager playlistRemote = new PlaylistManager(server.library);

        try {
            //Создаем заглушку объекта для отправки клиенту
            PlaylistManagerRMI stubPlaylist = (PlaylistManagerRMI) UnicastRemoteObject.exportObject(playlistRemote, 0);
            TrackManagerRMI stubTrack = (TrackManagerRMI) UnicastRemoteObject.exportObject(trackRemote, 0);

            //Регистрируем объект
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            //Связываем объект с индификатором
            registry.bind("playlist", stubPlaylist);
            registry.bind("track", stubTrack);

            System.out.println("Server start on port " + SERVER_PORT);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}