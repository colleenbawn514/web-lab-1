package com.lab1.client;

import com.lab1.common.Playlist;
import com.lab1.common.Track;
import com.lab1.exception.PlaylistNotFoundException;
import com.lab1.exception.TrackNotFoundException;
import com.lab1.interfaces.PlaylistManagerRemote;
import com.lab1.interfaces.TrackManagerRemote;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Utils {
    public PlaylistManagerRemote playlist;
    public TrackManagerRemote track;

    public Utils(ClientConnection client) {
        this.playlist = client.playlist;
        this.track = client.track;
    }

    public Playlist createPlaylistFromFile(String path, String name) throws FileNotFoundException, RemoteException, PlaylistNotFoundException {
        double size;
        int duration;
        String artist = "";
        String trackName;
        Playlist playlist = this.playlist.create(name);

        System.out.println("Create playlist [ID: " + playlist.getId() + ", Name: " + playlist.getName() + "]");

        FileReader reader = new FileReader(new File(path));
        Scanner scan = new Scanner(reader);
        scan.useLocale(Locale.ENGLISH);
        while (scan.hasNextLine()) {
            do {
                artist = scan.nextLine();
            } while (scan.hasNextLine() && artist.equals(""));
            if (!scan.hasNextLine()) break;
            trackName = scan.nextLine();
            duration = scan.nextInt();
            size = scan.nextDouble();
            //Track
            Track track = this.track.create(artist, trackName, size, duration);
            this.playlist.addTrack(playlist.getId(), track);
            System.out.println("Add track [artist: '" + artist + "' name: '" + trackName + "' size: " + size + "mb duration: " + duration + "sec]");
        }
        scan.close();
        System.out.println("All tracks added!");

        return playlist;
    }

    public void exportPlaylistToFile(int playlistId, String path) {
        try {
            FileWriter writer = new FileWriter(path, false);
            ArrayList<Integer> tracks = this.playlist.get(playlistId).getTrackIds();
            for (int id : tracks) {
                Track track = this.track.get(id);
                writer.write(track.getArtist() + "\n");
                writer.write(track.getName() + "\n");
                writer.write(track.getDuration() + "\n");
                writer.write(track.getSize() + "\n");
                writer.append('\n');
            }

            writer.flush();
            writer.close();
            System.out.println("Playlist success export");
        } catch (IOException | PlaylistNotFoundException | TrackNotFoundException e2) {
            System.out.println("Playlist failed export");
            System.out.println(e2.getMessage());
        }
    }

    public static void saveErrorLog(String task, String path, Exception e) {
        try {
            FileWriter writer = new FileWriter(path, false);

            writer.write("Task: '" + task + "'\n\n");
            writer.write("Error: ");
            writer.write((e.getMessage() != null ? e.getMessage() : e.toString()) + "\n\n");
            writer.write("Error stack trace: ");
            for (StackTraceElement err : e.getStackTrace()) {
                writer.write(err.toString() + "\n");
            }

            writer.flush();
            writer.close();
            System.out.println("Log saved");
        } catch (IOException e2) {
            System.out.println(e2.getMessage());
        }
    }
}
