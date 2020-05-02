package app.common;
import app.interfaces.TrackRMI;

import java.io.*;
import java.rmi.RemoteException;

public class Track implements Serializable, TrackRMI {
    private static final long serialVersionUID = 1;
    private String name;
    private int id;
    private double size;
    private int duration;
    private String author;

    /**
     * @param name     - название трека
     * @param size     - размер трека (МБ)
     * @param duration - длительность трека (сек)
     * @param id       - уникальный id трека
     * @param author   - автор трека
     */
    public Track(String author, String name, double size, int duration, int id ) throws IllegalArgumentException {
        if(name.trim().equals("")) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        this.name = name.trim();
        if(author.trim().equals("")) {
            throw new IllegalArgumentException("The author must not be empty");
        }
        this.author = author.trim();
        if(size <= 0) {
            throw new IllegalArgumentException("Size must be positive and greater than zero");
        }
        this.size = size;
        if(id < 0) {
            throw new IllegalArgumentException("Id must be positive and greater than zero");
        }
        this.id = id;
        if(duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive and greater than zero");
        }
        this.duration = duration;
    }

    public int getId() {
        return this.id;
    }

    public String getauthor() {
        return this.author;
    }

    public String getName() {
        return this.name;
    }

    public int getDuration() {
        return this.duration;
    }

    public double getSize() {
        return this.size;
    }

    public boolean equals(Track track) {
        return isEqual(track.author, track.name, track.duration, track.size);
    }

    public boolean isEqual(String author, String name, int duration, double size) {
        return this.author.equals(author)
            && this.name.equals(name)
            && this.duration == duration
            && this.size == size;
    }
}
