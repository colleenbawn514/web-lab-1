package com.lab1;

public class Track {

    private String name;
    private double size;
    private int duration;
    private int id;

    public Track(String name, double size, int duration, int id) {
        this.name = name;
        this.size = size;
        this.id = id;
        this.duration = duration;
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

    public double getSize() {
        return this.size;
    }

    public int getDuration() {
        return this.duration;
    }
}
