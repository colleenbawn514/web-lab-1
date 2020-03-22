package com.lab1.common;
import com.lab1.interfaces.TrackRMI;

import java.io.*;

public class Track implements Serializable, TrackRMI {
    private static final long serialVersionUID = 1;
    private String name;
    private int id;
    private double size;
    private int duration;
    private String artist;

    /**
     * @param name     - название трека
     * @param size     - размер трека (МБ)
     * @param duration - длительность трека (сек)
     * @param id       - уникальный id трека
     * @param artist   - автор трека
     */
    public Track(String artist, String name, double size, int duration, int id ) throws IllegalArgumentException {
        if(name.trim().equals("")) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        this.name = name.trim();
        if(artist.trim().equals("")) {
            throw new IllegalArgumentException("The artist must not be empty");
        }
        this.artist = artist.trim();
        if(size <= 0) {
            throw new IllegalArgumentException("Size must be positive and greater than zero");
        }
        this.size = size;
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive and greater than zero");
        }
        this.id = id;
        if(duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive and greater than zero");
        }
        this.duration = duration;
    }

    public String getName() {
        return this.name;
    }

    public String getArtist() {
        return this.artist;
    }

    public double getSize() {
        return this.size;
    }

    public int getId() {
        return this.id;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean equals(Track track) {
        return isEqual(track.artist, track.name, track.size, track.duration);
    }

    public boolean isEqual(String artist, String name, double size, int duration) {
        return this.name.equals(name)
                && this.duration == duration
                && this.size == size
                && this.artist.equals(artist);
    }
}
