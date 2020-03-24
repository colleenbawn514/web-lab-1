package com.lab1.common;

import com.lab1.interfaces.PlaylistRMI;
import java.io.*;
import java.util.ArrayList;

public class Playlist implements Serializable, PlaylistRMI {
    private static final long serialVersionUID = 1;
    private ArrayList<Integer> trackIds = new ArrayList<>();//номер трека
    private String name;
    private int id;

    public Playlist(String name, int id) {
        if(id < 0) {
            throw new IllegalArgumentException("Id must be positive and greater than zero");
        }
        this.id = id;
        if (name.trim().equals("")) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        this.name = name.trim();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.trackIds.size();
    }

    public ArrayList<Integer> getTrackIds() {
        return this.trackIds;
    }
}