package com.lab1.client;

import com.lab1.interfaces.PlaylistManagerRemote;
import com.lab1.interfaces.TrackManagerRemote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientConnection {
    public PlaylistManagerRemote playlist;
    public TrackManagerRemote track;

    public ClientConnection() {
        try {
            Registry registry = LocateRegistry.getRegistry(null, 80);
            this.track = (TrackManagerRemote) registry.lookup("tracksLibrary");
            this.playlist = (PlaylistManagerRemote) registry.lookup("playlistsLibrary");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.playlist != null || this.track != null;
    }
}
