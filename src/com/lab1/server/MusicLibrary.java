package com.lab1.server;

import com.lab1.common.*;
import com.lab1.exception.PlaylistNotFoundException;
import com.lab1.exception.TrackNotFoundException;
import com.lab1.exception.UserNotFoundException;
import com.lab1.interfaces.TrackManagerRemote;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MusicLibrary implements TrackManagerRemote {
    private Map<Integer, Track> tracks = new HashMap<>();
    private final DB db;

    public MusicLibrary(DB db) {
        this.db = db;
        try {
            this.db.execute(
                    "CREATE TABLE IF NOT EXISTS `tracks` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   name VARCHAR(20), " +
                            "   author INTEGER, " +
                            "   duration INTEGER, " +
                            "   size DOUBLE" +
                            " )"
            );
            this.db.execute(
                    "CREATE TABLE IF NOT EXISTS `authors` ( " +
                            "   id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "   name VARCHAR(20) UNIQUE" +
                            " )"
            );

        } catch (SQLException e) {
            System.err.println("Error get tracks from db");
            e.printStackTrace();
        }
    }

    public Track get(int trackId) throws TrackNotFoundException {
        boolean trackIsExist = this.isExistTrack(trackId);

        if (!trackIsExist) {
            System.out.println("Track " + trackId + " not found in DB");
            throw new TrackNotFoundException("Track " + trackId + " not found");
        }

        boolean tracksIsCached = this.tracks.containsKey(trackId);
        if (!tracksIsCached) {
            try {
                ResultSet result = this.db.executeQuery(
                        "SELECT `tracks`.`id`," +
                                " `authors`.`name` AS `author`," +
                                " `tracks`.`name`," +
                                " `tracks`.`duration`," +
                                " `tracks`.`size`" +
                                "  FROM `tracks`, `authors` " +
                                " WHERE  `tracks`.`id`='" + trackId + "' " +
                                "AND `tracks`.`author`=`authors`.`id`"
                );
                if (result.next()) {
                    int id = result.getInt("id");
                    String author = result.getString("author");
                    String name = result.getString("name");
                    int duration = result.getInt("duration");
                    double size = result.getDouble("size");
                    Track track = new Track(
                            author,
                            name,
                            size,
                            duration,
                            id
                    );
                    this.tracks.put(trackId, track);
                    tracksIsCached = true;
                } else {
                    tracksIsCached = false;
                }
            } catch (SQLException e) {
                System.err.println("Error get playlist from db");
                e.printStackTrace();
            }
        }
        if (!tracksIsCached) {
            throw new TrackNotFoundException("Track ID:" + trackId + " not found");
        }

        return this.tracks.get(trackId);
    }

    public Track create(String author, String name, double size, int duration) throws TrackNotFoundException {
        Integer trackId = null;
        Integer authorId = 0;
        try {
            ResultSet result = this.db.executeQuery(
                    "SELECT `tracks`.`id`," +
                            " `authors`.`name` AS `author`," +
                            " `tracks`.`name`," +
                            " `tracks`.`duration`," +
                            " `tracks`.`size`" +
                            "  FROM `tracks`, `authors` " +
                            " WHERE  `tracks`.`author`=`authors`.`id` AND " +
                            "`authors`.`name`='" + author + "' AND `tracks`.`name`='" + name +
                            "' AND `tracks`.`size`='" + size + "' AND `tracks`.`duration`='" + duration + "'"
            );
            if(result.next()){
                trackId = result.getInt("id");
            }
            System.out.println("Track " + author + " " + name + " in DB: " + (trackId!=null? " all ready exist" : "not exist"));
            if(trackId!=null){
                return this.get(trackId);
            }


            Track track = null;
            try {
                try{
                    this.db.execute(
                        "INSERT INTO `authors` (`name`) VALUES ('" + author + "')"
                    );
                    System.out.println("Author " + author + " add in DB");
                } catch (SQLException e){
                    System.out.println("Author " + author + " already exist");
                }

                ResultSet resultAuthorId = this.db.executeQuery(
                        "SELECT `id` FROM `authors` WHERE `name`='" + author + "'"
                );
                resultAuthorId.next();
                authorId = resultAuthorId.getInt("id");
                this.db.execute(
                        "INSERT INTO `tracks` " +
                                "    (author, `name`, duration, size) " +
                                "VALUES " +
                                "    ('" + authorId + "', '" + name + "', " + duration + ", " + size + ");"
                );
                ResultSet resultTrack = this.db.executeQuery("SELECT MAX(id) FROM `tracks`");
                if(resultTrack.next()){
                    trackId = result.getInt(1);
                } else {
                    trackId = 1;
                }
                track = new Track(author, name, size, duration, trackId);
                this.tracks.put(trackId, track);

            } catch (SQLException e) {
                System.err.println("Error write do db");
                e.printStackTrace();
            }
            System.out.println("Add track " + author + " " + name + " to DB");

            return track;
        } catch (SQLException e) {
            System.err.println("Error get tracks from db");
            e.printStackTrace();
            return null;
        }
    }

    public boolean isExistTrack(int trackId) throws TrackNotFoundException {
        boolean trackIsExist = this.tracks.containsKey(trackId);
        if (!trackIsExist) {
            System.out.println("Track " + trackId + " not find in cache. Search in DB");
            try {
                ResultSet result = this.db.executeQuery("SELECT id FROM `tracks` WHERE `id`='" + trackId + "'");
                trackIsExist = result.next();
                System.out.println("Track " + trackId + " in DB: " + (trackIsExist ? "found" : "not found"));
            } catch (SQLException e) {
                System.err.println("Error get tracks from db");
                e.printStackTrace();
            }
        }
        return trackIsExist;
    }
}



