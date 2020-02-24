package com.lab1;
import java.io.*;

public class Track implements Serializable{
    private static final long serialVersionUID = 1;
    private String name;
    private double size;
    private int duration;

    /**
     * @param name     - название трека
     * @param size     - размер трека (МБ)
     * @param duration - длительность трека (сек)
     */
    public Track(String name, double size, int duration) throws IllegalArgumentException {
        if(name.trim().equals("")) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        this.name = name.trim();
        if(size <= 0) {
            throw new IllegalArgumentException("Size must be positive and greater than zero");
        }
        this.size = size;
        if(duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive and greater than zero");
        }
        this.duration = duration;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setSize(double newSize) {
        this.size = newSize;
    }

    public void setDuration(int newDuration) {
        this.duration = newDuration;
    }

    public double getSize() {
        return this.size;
    }

    public int getDuration() {
        return this.duration;
    }

    public void exportToFile(String path) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();

    }
    public boolean equals(Track track) {
        return this.name.equals(track.name)  && this.duration == track.duration && this.size == track.size;
    }

    static Track importFromFile(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Track) objectInputStream.readObject();
    }
}
