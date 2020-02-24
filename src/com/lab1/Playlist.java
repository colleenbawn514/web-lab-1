package com.lab1;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Playlist {
    
    private Map<Integer, Track> tracks = new HashMap<>();//трек
    private ArrayList<Integer> trackIds = new ArrayList<>();//номер трека
    private int trackIdsIterator = 0;
    private String name;
    private int id;

    public Playlist(String name, int id) {
        if(name.trim().equals("")) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        this.name = name.trim();
        this.id = id;
    }

    public Playlist(String name, int id, Map<Integer, Track> tracks) {
        this.name = name;
        this.id = id;
        this.tracks = tracks;
    }

    public int getId() {
        return this.id;
    }

    public int getCountsTracks() {
        return this.trackIds.size();
    }

    public int addTrack(Track track) {
        int trackId = getNextTrackId();
        this.tracks.put(trackId, track);
        this.trackIds.add(trackId);

        return trackId;
    }

    public int addTrack(Track track, int index) {
        int trackId = getNextTrackId();
        this.tracks.put(trackId, track);
        this.trackIds.add(index, trackId);

        return trackId;
    }

    public void removeTrack(int trackId) {
        checkExistTrack(trackId);

        this.trackIds.remove(this.trackIds.indexOf(trackId));
        this.tracks.remove(trackId);
    }

    public Track getTrack(int trackId) {
        checkExistTrack(trackId);

        return this.tracks.get(trackId);
    }

    public int[] getTrackIds() {
        return this.trackIds.stream().mapToInt(Number::intValue).toArray();//из списка в массив
    }

    public void exportToFile(String path) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }

    static Playlist importFromFile(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Playlist) objectInputStream.readObject();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void sort(boolean isAsc) {
        Comparator<Integer> comparator = Comparator.comparing(id -> this.tracks.get(id).getName());

        if (isAsc) {
            this.trackIds.sort(comparator);
        } else {
            this.trackIds.sort(comparator.reversed());
        }
    }
    public void duplicateTrackRemoval() {
        int aId = 0;
        int bId;
        for(int i = 0; i<this.trackIds.size(); i++){
            for( int j=i+1; j<this.trackIds.size(); j++){

                boolean isSame = this.tracks.get(this.trackIds.get(i)).equals(this.tracks.get(this.trackIds.get(j)));
                if(isSame){
                    System.out.println("Remove " + j + " name " + this.tracks.get(this.trackIds.get(j)).getName());
                    this.tracks.remove(this.trackIds.get(j));
                    this.trackIds.remove(j);
                    j--;
                }

            }
        }
    }
    private int getNextTrackId(){//следующий трек
        return ++trackIdsIterator;
    }

    private void checkExistTrack(int trackId) {
        if (!this.trackIds.contains(trackId)) {
            throw new NullPointerException("Track with such ID does not exist");
        }
    }
}
