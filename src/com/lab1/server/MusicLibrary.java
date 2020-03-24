package com.lab1.server;

import com.lab1.common.*;
import com.lab1.exception.TrackNotFoundException;
import com.lab1.interfaces.TrackManagerRemote;

import java.util.HashMap;
import java.util.Map;

public class MusicLibrary implements TrackManagerRemote {
    private Map<Integer, Track> tracks = new HashMap<>();
    private int maxId = 0;

    public Track get(int trackId) throws TrackNotFoundException {
        if (!this.tracks.containsKey(trackId)) {
            throw new TrackNotFoundException("Track " + trackId + " not found");
        }
        return tracks.get(trackId);
    }

    public Track create(String artist, String name, double size, int duration) {
        for (Map.Entry<Integer, Track> entry : tracks.entrySet()) {
            Track track = entry.getValue();
            if (track.isEqual(artist, name, duration, size)) {
                return track;
            }
        }
        Track track = new Track(artist, name, size, duration, maxId);
        this.tracks.put(maxId, track);
        this.maxId += 1;
        return track;
    }
}
