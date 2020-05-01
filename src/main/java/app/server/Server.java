package com.lab1.server;

import com.lab1.interfaces.PlaylistManagerRemote;
import com.lab1.interfaces.TrackManagerRemote;
import com.lab1.interfaces.UserManagerRemote;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class Server{
    private final MusicLibrary musicLibrary;
    private final PlaylistLibrary playlistLibrary;
    private final UserLibrary userLibrary;
    private final DB db;
    static private final int SERVER_PORT = 80;

    public Server() throws ClassNotFoundException, SQLException {
        this.db = DB.connection("colleen.music");
        this.musicLibrary = new MusicLibrary(this.db);
        this.userLibrary = new UserLibrary(this.db);
        this.playlistLibrary = new PlaylistLibrary(this.musicLibrary, this.userLibrary, this.db);
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            //Создаем заглушку объекта для отправки клиенту
            TrackManagerRemote stubMusicLibrary = (TrackManagerRemote)
                UnicastRemoteObject.exportObject(server.musicLibrary, 0);
            PlaylistManagerRemote stubPlaylistLibrary = (PlaylistManagerRemote)
                UnicastRemoteObject.exportObject(server.playlistLibrary, 0);
            UserManagerRemote stubUserLibrary = (UserManagerRemote)
                    UnicastRemoteObject.exportObject(server.userLibrary, 0);

            //Регистрируем объект
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            //Связываем объект с индификатором
            registry.bind("tracksLibrary", stubMusicLibrary);
            registry.bind("playlistsLibrary", stubPlaylistLibrary);
            registry.bind("usersLibrary", stubUserLibrary);

            System.out.println("Server start on port " + SERVER_PORT);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}