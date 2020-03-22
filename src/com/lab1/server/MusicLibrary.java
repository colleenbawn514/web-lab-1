package com.lab1.server;

import com.lab1.common.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MusicLibrary {
    private static final long serialVersionUID = 1;
    private Map<Integer, Track> tracks = new HashMap<>();//трек
    private int maxId = 0;

    public Track getTrack(int trackId) {
        return tracks.get(trackId);
    }

    public Track addTrack(String artist, String name, double size, int duration) {
        for (Map.Entry<Integer, Track> entry : tracks.entrySet()) {
            Track track = entry.getValue();
            if (track.isEqual(artist, name, size, duration)) {
                return track;
            }
        }
        ++maxId;
        Track track = new Track(artist, name, size, duration, maxId);
        this.tracks.put(maxId, track);
        return track;
    }
}
