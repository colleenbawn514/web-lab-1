package com.lab1;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private MusicLibrary task;

    public Client() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(null, 80);
            this.task = (MusicLibrary) registry.lookup("playlists");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.task != null;
    }

    public static void main(String[] args) throws RemoteException {
        boolean isExit = false;
        Client client = new Client();

        if (client.isConnected()) {
            Scanner s = new Scanner(System.in);
            while (!isExit) {
                System.out.print("> ");

                switch (s.nextLine()) {
                    case "create p":
                        System.out.print("Playlist name: ");
                        client.task.createPlaylist(s.nextLine());
                        break;
                    case "create p --file":
                        System.out.print("Path: ");
                        String path = s.nextLine();
                        System.out.print("Playlist name: ");
                        String name = s.nextLine();
                        double size;
                        int duration;
                        int playlistId = client.task.createPlaylist(name);
                        try {
                            FileReader reader = new FileReader(new File(path));
                            Scanner scan = new Scanner(reader);
                            while (scan.hasNextLine()) {
                                name = scan.nextLine();
                                size = scan.nextDouble();
                                duration = scan.nextInt();
                                client.task.addTrack(playlistId, name, size, duration);
                                System.out.println("Add track [name: '" + name + "' size: " + size + "mb duration: " + duration + "sec]");
                                scan.nextLine();
                                if (scan.hasNextLine()) scan.nextLine();
                            }
                            scan.close();
                            System.out.println("All tracks added!");
                        } catch (FileNotFoundException e) {
                            System.out.print("File not found. Create [y|n]?: ");
                            boolean isCreate = s.nextLine().equals("y");

                            if (isCreate) {
                                try {
                                    FileWriter writer = new FileWriter(path, false);
                                    writer.flush();
                                } catch (IOException e2) {
                                    System.out.println(e2.getMessage());
                                }
                            }
                        }

                        break;
                    case "create t":
                        System.out.print("Playlist ID: ");
                        playlistId = s.nextInt();
                        System.out.print("Track name: ");
                        s.nextLine();
                        name = s.nextLine();
                        System.out.print("Track size: ");
                        size = s.nextDouble();
                        System.out.print("Track duration: ");
                        s.nextLine();
                        duration = s.nextInt();
                        s.nextLine();
                        client.task.addTrack(playlistId, name, size, duration);
                        break;
                    case "get p --all":
                        int[] playlists = client.task.getPlaylistIds();
                        System.out.println("ID    | Name");
                        for (int id : playlists) {
                            System.out.println(String.format(
                                    "%-6s| %s",
                                    id,
                                    client.task.getPlaylistNameById(id)
                            ));
                        }
                        break;
                    case "get p --file":
                        System.out.print("Playlist ID: ");
                        playlistId = s.nextInt();
                        s.nextLine();
                        System.out.print("Path: ");
                        path = s.nextLine();

                        try {
                            FileWriter writer = new FileWriter(path, false);

                            int[] tracks = client.task.getPlaylistTrackIdsById(playlistId);
                            for (int id : tracks) {
                                writer.write(client.task.getTrackNameById(playlistId, id));
                                writer.append('\n');
                                writer.write(client.task.getTrackDurationById(playlistId, id)+"");
                                writer.append('\n');
                                writer.write(client.task.getTrackSizeById(playlistId, id)+"");
                                writer.append('\n');
                                writer.append('\n');
                            }

                            writer.flush();
                            System.out.println("Playlist success export");
                        } catch (IOException e2) {
                            System.out.println("Playlist failed export");
                            System.out.println(e2.getMessage());
                        }
                        break;
                    case "get p":
                        System.out.print("Playlist ID: ");
                        playlistId = s.nextInt();
                        s.nextLine();
                        System.out.println("Name: '" + client.task.getPlaylistNameById(playlistId) + "'");

                        int[] tracks = client.task.getPlaylistTrackIdsById(playlistId);
                        System.out.println("ID    | Name                 | Duration(Sec)   | Size(MB)");
                        for (int id : tracks) {
                            System.out.println(String.format(
                                    "%-6s| %-20s | %-15s | %s",
                                    id,
                                    client.task.getTrackNameById(playlistId, id),
                                    client.task.getTrackDurationById(playlistId, id),
                                    client.task.getTrackSizeById(playlistId, id)
                            ));
                        }
                        break;
                    case "edit p --sort":
                        System.out.print("Playlist ID: ");
                        playlistId = s.nextInt();
                        s.nextLine();
                        System.out.print("Sort by Asc [y|n]?: ");
                        boolean isAsc = s.nextLine().equals("y");

                        client.task.sortPlaylist(playlistId, isAsc);

                        System.out.println("Playlist sorted by " + (isAsc ? "ASC" : "DESC"));
                        break;
                    case "exit":
                        isExit = true;
                        s.close();
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
        }
    }
}