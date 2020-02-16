package com.lab1;

public class Playlist {

    private int[] tracksIds;
    private String name;
    private int id;
    private int size;

    public Playlist(String name, int id) throws Exception {
        if(name.equals("")) throw new Exception("Playlist title must not be empty");
        this.name = name;
        this.id = id;
        this.size = 0;
        this.tracksIds = new int[100];
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int[] getTracks() {
        return this.tracksIds;
    }

    public void addTrack(int trackId) {
        this.size++;
        this.tracksIds[this.size] = trackId;
    }
}
