package com.lab1.server;

import com.lab1.common.*;
import com.lab1.exception.TrackNotFoundException;
import com.lab1.interfaces.TrackManagerRemote;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MusicLibrary implements TrackManagerRemote {
    private Map<Integer, Track> tracks = new HashMap<>();
    private int maxId = 1;
    private final DB db;

    public MusicLibrary(DB db) {
        this.db = db;
        try {
            this.db.execute(
                    "CREATE TABLE IF NOT EXISTS `tracks` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   author VARCHAR(20), " +
                            "   name VARCHAR(20), " +
                            "   duration INTEGER, " +
                            "   size DOUBLE" +
                            " )"
            );

            ResultSet result = this.db.executeQuery("SELECT * FROM `tracks`");
            int size = 0;

            System.out.println("Load tracks");
            while (result.next()) {
                int count = result.getMetaData().getColumnCount();
                for (int i=1; i<count+1; i++){
                    System.out.print(result.getString(i)+"  |  ");
                }
                System.out.println();

                Track track = new Track(
                        result.getString(2),
                        result.getString(3),
                        Double.parseDouble(result.getString(5)),
                        Integer.parseInt(result.getString(4)),
                        Integer.parseInt(result.getString(1))
                );
                size += 1;
                this.tracks.put(Integer.parseInt(result.getString(1)), track);
            }
            result = this.db.executeQuery("SELECT MAX(id) FROM `tracks`");
            if (result.getString(1) != null) {
                this.maxId = Integer.parseInt(result.getString(1)) + 1;
            }
            System.out.println("Load " + size + " tracks to cache");
        } catch (SQLException e) {
            System.err.println("Error get tracks from db");
            e.printStackTrace();
        }
    }

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
        Track track = new Track(artist, name, size, duration, this.maxId);
        this.tracks.put(this.maxId, track);
        try {
            this.db.execute(
                    "INSERT INTO `tracks` " +
                            "    (author, `name`, duration, size) " +
                            "VALUES " +
                            "    ('" + artist + "', '" + name + "', " + duration + ", " + size + ");"
            );
        } catch (SQLException e) {
            System.err.println("Error write do db");
            e.printStackTrace();
        }
        this.maxId += 1;
        return track;
    }
}
