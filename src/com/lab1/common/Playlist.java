package com.lab1.common;

import com.lab1.interfaces.PlaylistRMI;
import com.lab1.server.MusicLibrary;

import java.io.*;
import java.util.ArrayList;

public class Playlist implements Serializable, PlaylistRMI {
    private static final long serialVersionUID = 1;
    private ArrayList<Integer> trackIds = new ArrayList<>();//номер трека
    private String name;
    private int id;

    public Playlist(String name, int id, MusicLibrary library) {
        if (name.trim().equals("")) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        this.name = name.trim();
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public int getCountsTracks() {
        return this.trackIds.size();
    }

    public ArrayList<Integer> getTrackIds() {
        return this.trackIds;//из списка в массив
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}