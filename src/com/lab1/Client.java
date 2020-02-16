package com.lab1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Client {

    private Client() {}

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(null, 80);
            MusicLibrary stub = (MusicLibrary) registry.lookup("music");

            stub.addTrack("Дайте танк! - Утро", 3400, 3 * 60 + 40);
            stub.createPlaylist("Мой плейлист");

            int[] playlists = stub.getPlaylistsIds();
            System.out.print("Плейлисты: " + Arrays.toString(playlists));
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}