package com.lab1.client;

import com.lab1.common.Playlist;
import com.lab1.interfaces.*;
import com.lab1.common.Track;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private ServerRMI server;
    private PlaylistManagerRMI playlistManager;
    private TrackManagerRMI trackManager;

    public Client() {
        try {
            Registry registry = LocateRegistry.getRegistry(null, 80);
            this.server = (ServerRMI) registry.lookup("server");
            this.playlistManager = (PlaylistManagerRMI) registry.lookup("playlist");
            this.trackManager = (TrackManagerRMI) registry.lookup("track");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.server != null || this.playlistManager != null || this.trackManager != null;
    }

    public Playlist createPlaylistFromFile(String path, String name) throws FileNotFoundException, RemoteException {
        double size;
        int duration;
        String artist = "";
        String trackName;
        Playlist playlist = this.playlistManager.create(name);

        FileReader reader = new FileReader(new File(path));
        Scanner scan = new Scanner(reader);
        scan.useLocale(Locale.ENGLISH);
        if (scan.hasNextLine()) artist = scan.nextLine();
        while (scan.hasNextLine()) {
            trackName = scan.nextLine();
            duration = scan.nextInt();
            size = scan.nextDouble();
            //Track
            Track track = this.trackManager.create(artist, trackName, size, duration);
            playlistManager.addTrack(playlist.getId(), track);
            System.out.println("Add track [artist: '" + artist + "' name: '" + name + "' size: " + size + "mb duration: " + duration + "sec]");
            artist = scan.nextLine();
            while (scan.hasNextLine() && artist.equals("")) {
                artist = scan.nextLine();
            }
        }
        scan.close();
        System.out.println("All tracks added!");

        return playlist;
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

    public void exportPlaylistToFile(int playlistId, String path) {
        try {
            FileWriter writer = new FileWriter(path, false);
            Map<Integer, Track> tracks = this.playlistManager.getTracks(playlistId);
            for (int id : tracks.keySet()) {
                writer.write(tracks.get(id).getArtist() + "\n");
                writer.write(tracks.get(id).getName() + "\n");
                writer.write(tracks.get(id).getDuration() + "\n");
                writer.write(tracks.get(id).getSize() + "\n");
                writer.append('\n');
            }

            writer.flush();
            writer.close();
            System.out.println("Playlist success export");
        } catch (IOException e2) {
            System.out.println("Playlist failed export");
            System.out.println(e2.getMessage());
        }
    }

    public static void main(String[] args) throws RemoteException, FileNotFoundException, ClassNotFoundException {
        boolean isExit = false;
        Client client = new Client();
        System.out.println("Для выполнения задания напишите 'run task' или 'help' чтобы посмотреть помощь");
        if (client.isConnected()) {
            Scanner console = new Scanner(System.in);
            while (!isExit) {
                System.out.print("> ");

                switch (console.nextLine().trim()) {
                    case "create p"://создать плейлист
                        System.out.print("Playlist name: ");
                        client.playlistManager.create(console.nextLine());
                        break;
                    case "create p --file"://создать плейлист из файла
                        System.out.print("Path: ");
                        String path = console.nextLine();
                        System.out.print("Playlist name: ");
                        String name = console.nextLine();
                        client.createPlaylistFromFile(path, name);
                        break;
                    case "create t"://создать трек
                        System.out.print("Playlist ID: ");
                        Playlist playlist = client.playlistManager.get(console.nextInt());
                        System.out.print("Track artist: ");
                        console.nextLine();
                        String artist = console.nextLine();
                        System.out.print("Track name: ");
                        name = console.nextLine();
                        System.out.print("Track size: ");
                        double size = console.nextDouble();
                        System.out.print("Track duration: ");
                        console.nextLine();
                        int duration = console.nextInt();
                        console.nextLine();
                        Track track = client.trackManager.create(artist, name, size, duration);
                        client.playlistManager.addTrack(playlist.getId(), track);
                        break;
                    case "get p --all"://вывести все плейлисты
                        ArrayList<Integer> playlists = client.playlistManager.getAllIds();
                        System.out.println("ID    | Name");
                        for (int id : playlists) {
                            System.out.println(String.format( //форматированные строки
                                    "%-6s| %s",
                                    id,
                                    client.playlistManager.get(id).getName()
                            ));
                        }
                        break;
                    case "get p --file"://вывести плейлист в файл
                        System.out.print("Playlist ID: ");
                        int playlistId = console.nextInt();
                        console.nextLine();
                        System.out.print("Path: ");
                        path = console.nextLine();
                        client.exportPlaylistToFile(playlistId, path);
                        break;
                    case "get p"://вывести плейлист
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();
                        playlist = client.playlistManager.get(playlistId);
                        System.out.println("Name: '" + playlist.getName() + "'");

                        Map<Integer, Track> tracks = client.playlistManager.getTracks(playlistId);
                        System.out.println(tracks.size());
                        System.out.println("ID    | Artist         | Name             | Duration(Sec)     | Size(MB)");
                        for (int id : tracks.keySet()) {
                            System.out.println(String.format(
                                    "%-6s| %-14s | %-16s | %-17s | %s",
                                    id,
                                    tracks.get(id).getArtist(),
                                    tracks.get(id).getName(),
                                    tracks.get(id).getDuration(),
                                    tracks.get(id).getSize()
                            ));
                        }
                        break;
                    case "edit p --sort"://сортировка
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();
                        System.out.print("Sort by Asc [y|n]?: ");
                        boolean isAsc = console.nextLine().equals("y");

                        client.playlistManager.sort(playlistId, isAsc);

                        System.out.println("Playlist sorted by " + (isAsc ? "ASC" : "DESC"));
                        break;
                    case "edit p --duplicate-remove":
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();

                        client.playlistManager.removeDuplicate(playlistId);
                        break;
                    case "run task":
                        try {
                            System.out.print("Path: ");
                            path = console.nextLine();
                            System.out.print("Playlist name: ");
                            name = console.nextLine();
                            playlist = client.createPlaylistFromFile(path, name);

                            client.playlistManager.removeDuplicate(playlist.getId());

                            System.out.print("Sort by Asc [y|n]?: ");
                            isAsc = console.nextLine().equals("y");

                            client.playlistManager.sort(playlist.getId(), isAsc);

                            System.out.print("Save path [" + playlist.getName() + "]: ");
                            path = console.nextLine();
                            client.exportPlaylistToFile(playlist.getId(), path.equals("") ? playlist.getName() : path);
                        } catch (Exception e) {
                            System.out.print("Error! Save log path[log.txt]: ");
                            path = console.nextLine();
                            saveErrorLog("run task", path.equals("") ? "log.txt" : path, e);
                        }
                        break;
                    case "help":
                        System.out.print("create p                      Создать плейлист \n");
                        System.out.print("create p --file               Создать плейлист из файла \n");
                        System.out.print("create t                      Создать трек \n");
                        System.out.print("get p                         Вывести плейлист \n");
                        System.out.print("get p --all                   Вывести все плейлисты \n");
                        System.out.print("get p --file                  Вывести плейлист в файл \n");
                        System.out.print("edit p --sort                 Сортировка плейлиста \n");
                        System.out.print("edit p --duplicate-remove     Удаление дупликатов \n");
                        System.out.print("run task                      Выполнение задания лабораторной \n");
                        System.out.print("exit                          Выход \n");
                        break;
                    case "exit":
                        isExit = true;
                        console.close();
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
        }
    }
}