package com.lab1;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Locale;
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

    public int createPlaylistFromFile(String path, String name) throws RemoteException, FileNotFoundException {
        double size;
        int duration;
        int playlistId = this.task.createPlaylist(name);

        FileReader reader = new FileReader(new File(path));
        Scanner scan = new Scanner(reader);
        scan.useLocale(Locale.ENGLISH);
        if (scan.hasNextLine()) name = scan.nextLine();
        while (scan.hasNextLine()) {
            System.out.println("NAME: '"+name+"'");
            duration = scan.nextInt();
            size = scan.nextDouble();
            this.task.addTrack(playlistId, name, size, duration);
            System.out.println("Add track [name: '" + name + "' size: " + size + "mb duration: " + duration + "sec]");
            name = scan.nextLine();
            while (scan.hasNextLine() && name.equals("")) {
                name = scan.nextLine();
            }
        }
        scan.close();
        System.out.println("All tracks added!");

        return playlistId;
    }

    public static void saveErrorLog(String task, String path, Exception e) {
        try {
            FileWriter writer = new FileWriter(path, false);

            writer.write("Task: '"+task+"'\n\n");
            writer.write("Error: ");
            writer.write((e.getMessage() != null? e.getMessage() : e.toString()) + "\n\n");
            writer.write("Error stack trace: ");
            for(StackTraceElement err : e.getStackTrace()) {
                writer.write(err.toString()+"\n");
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

            int[] tracks = this.task.getPlaylistTrackIdsById(playlistId);
            for (int id : tracks) {
                writer.write(this.task.getTrackNameById(playlistId, id) + "\n");
                writer.write(this.task.getTrackDurationById(playlistId, id) + "\n");
                writer.write(this.task.getTrackSizeById(playlistId, id) + "\n");
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

    public static void main(String[] args) throws RemoteException, FileNotFoundException {
        boolean isExit = false;
        Client client = new Client();
        System.out.print("Для выполнения задания напишите 'run task' или 'help' чтобы посмотреть помощь");
        if (client.isConnected()) {
            Scanner console = new Scanner(System.in);
            while (!isExit) {
                System.out.print("> ");

                switch (console.nextLine().trim()) {
                    case "create p"://создать плейлист
                        System.out.print("Playlist name: ");
                        client.task.createPlaylist(console.nextLine());
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
                        int playlistId = console.nextInt();
                        System.out.print("Track name: ");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.print("Track size: ");
                        double size = console.nextDouble();
                        System.out.print("Track duration: ");
                        console.nextLine();
                        int duration = console.nextInt();
                        console.nextLine();
                        client.task.addTrack(playlistId, name, size, duration);
                        break;
                    case "get p --all"://вывести все плейлисты
                        int[] playlists = client.task.getPlaylistIds();
                        System.out.println("ID    | Name");
                        for (int id : playlists) {
                            System.out.println(String.format( //форматированные строки
                                    "%-6s| %s",
                                    id,
                                    client.task.getPlaylistNameById(id)
                            ));
                        }
                        break;
                    case "get p --file"://вывести плейлист в файл
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();
                        System.out.print("Path: ");
                        path = console.nextLine();
                        client.exportPlaylistToFile(playlistId, path);
                        break;
                    case "get p"://вывести плейлист
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();
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
                    case "edit p --sort"://сортировка
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();
                        System.out.print("Sort by Asc [y|n]?: ");
                        boolean isAsc = console.nextLine().equals("y");

                        client.task.sortPlaylist(playlistId, isAsc);

                        System.out.println("Playlist sorted by " + (isAsc ? "ASC" : "DESC"));
                        break;
                    case "edit p --duplicate-remove":
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();

                        client.task.duplicateTrackRemovalPlaylist(playlistId);
                        break;
                    case "run task":
                        try {
                            System.out.print("Path: ");
                            path = console.nextLine();
                            System.out.print("Playlist name: ");
                            name = console.nextLine();
                            playlistId = client.createPlaylistFromFile(path, name);

                            client.task.duplicateTrackRemovalPlaylist(playlistId);

                            System.out.print("Sort by Asc [y|n]?: ");
                            isAsc = console.nextLine().equals("y");

                            client.task.sortPlaylist(playlistId, isAsc);

                            System.out.print("Save path [" + client.task.getPlaylistNameById(playlistId) + "]: ");
                            path = console.nextLine();
                            client.exportPlaylistToFile(playlistId, path.equals("") ? client.task.getPlaylistNameById(playlistId) : path);
                        } catch (Exception e) {
                            System.out.print("Error! Save log path[log.txt]: ");
                            path = console.nextLine();
                            saveErrorLog("run task", path.equals("")? "log.txt" : path, e);
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