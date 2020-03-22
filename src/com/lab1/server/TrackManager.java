package com.lab1.server;
import com.lab1.interfaces.*;
import com.lab1.common.*;

public class TrackManager implements TrackManagerRMI {
    private final MusicLibrary library;

    public TrackManager(MusicLibrary library) {
        this.library = library;
    }

    public Track get(int trackId) {
        return this.library.getTrack(trackId);
    }

    public Track create(String artist, String name, double size, int duration) {
        return this.library.addTrack(artist, name, size, duration);
    }
}
