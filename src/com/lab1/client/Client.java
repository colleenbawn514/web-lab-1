package com.lab1.client;

import com.lab1.common.Playlist;
import com.lab1.common.User;
import com.lab1.exception.PlaylistNotFoundException;
import com.lab1.exception.TrackNotFoundException;
import com.lab1.common.Track;
import com.lab1.exception.UserNotFoundException;

import javax.naming.AuthenticationException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Client extends ClientConnection {
    public Utils utils;

    public Client() {
        super();
        this.utils = new Utils(this);
    }

    public static void main(String[] args) throws RemoteException, FileNotFoundException, PlaylistNotFoundException, TrackNotFoundException, UserNotFoundException {
        boolean isExit = false;
        Client client = new Client();
        User user = null;
        if (client.isConnected()) {
            Scanner console = new Scanner(System.in);
            System.out.println("Войдите или зарегестрируйтесь");
            while (user == null) {
                System.out.print("Логин: ");
                String userLogin = console.nextLine();
                boolean isExistUser = client.user.isExistUser(userLogin);
                if (isExistUser) {
                    System.out.print("Пароль: ");
                } else {
                    System.out.print("Такого пользователя еще нет, зарегестрировать [y|n]?: ");
                    if (!console.nextLine().equals("y")) {
                        System.out.println("Войдите в систему");
                        continue;
                    }
                    System.out.print("Для регистрации введите пароль: ");
                }
                String userPassword = console.nextLine();
                if (!isExistUser) {
                    System.out.print("Введите ваше имя: ");
                    String userName = console.nextLine();

                    try {
                        user = client.user.create(userLogin, userPassword, userName);
                    }catch (IllegalArgumentException e){
                        System.err.println(e.getMessage());
                        System.out.println("Ошибка созадния пользователя. Попробуйте снова");
                    }
                } else {
                    try {
                        user = client.user.get(userLogin, userPassword);
                    }catch (UserNotFoundException | AuthenticationException e){
                        System.err.println(e.getMessage());
                        System.out.println("Попробуйте снова");
                    }
                }
            }
            System.out.println("Добро пожаловать, "+user.getName());
            System.out.println("Для выполнения задания напишите 'run task' или 'help' чтобы посмотреть помощь");
            while (!isExit) {
                System.out.print("> ");

                switch (console.nextLine().trim()) {
                    case "create p"://создать плейлист
                        System.out.print("Playlist name: ");
                        client.playlist.create(user.getId(), console.nextLine());
                        break;
                    case "create p --file"://создать плейлист из файла
                        System.out.print("Path: ");
                        String path = console.nextLine();
                        System.out.print("Playlist name: ");
                        String name = console.nextLine();
                        client.utils.createPlaylistFromFile(user, path, name);
                        break;
                    case "create t"://создать трек
                        System.out.print("Playlist ID: ");
                        Playlist playlist = client.playlist.get(user.getId(), console.nextInt());
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
                        Track track = client.track.create(artist, name, size, duration);
                        client.playlist.addTrack(user.getId(), playlist.getId(), track);
                        break;
                    case "get p --all"://вывести все плейлисты
                        Map<Integer, String> playlistsInfo = client.playlist.getAll(user.getId());
                        System.out.println("ID    | Name");
                        for (int id : playlistsInfo.keySet()) {
                            System.out.println(String.format( //форматированные строки
                                    "%-6s| %s",
                                    id,
                                    playlistsInfo.get(id)
                            ));
                        }
                        break;
                    case "get p --file"://вывести плейлист в файл
                        System.out.print("Playlist ID: ");
                        int playlistId = console.nextInt();
                        console.nextLine();
                        System.out.print("Path: ");
                        path = console.nextLine();
                        client.utils.exportPlaylistToFile(user, playlistId, path);
                        break;
                    case "get p"://вывести плейлист
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();
                        playlist = client.playlist.get(user.getId(), playlistId);
                        System.out.println("Name: '" + playlist.getName() + "'");

                        ArrayList<Integer> tracks = client.playlist.get(user.getId(), playlistId).getTrackIds();
                        System.out.println(tracks.size());
                        System.out.println("ID  | Artist                | Name                  | Duration(Sec)     | Size(MB)");
                        for (int id : tracks) {
                            track = client.track.get(id);
                            System.out.println(String.format(
                                    "%-4s| %-21s | %-21s | %-17s | %s",
                                    id,
                                    track.getArtist(),
                                    track.getName(),
                                    track.getDuration(),
                                    track.getSize()
                            ));
                        }
                        break;
                    case "edit p --sort"://сортировка
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();
                        System.out.print("Sort by Asc [y|n]?: ");
                        boolean isAsc = console.nextLine().equals("y");

                        client.playlist.sort(user.getId(), playlistId, isAsc);

                        System.out.println("Playlist sorted by " + (isAsc ? "ASC" : "DESC"));
                        break;
                    case "edit p --duplicate-remove":
                        System.out.print("Playlist ID: ");
                        playlistId = console.nextInt();
                        console.nextLine();

                        client.playlist.removeDuplicate(user.getId(), playlistId);
                        break;
                    case "run task":
                        try {
                            System.out.print("Path: ");
                            path = console.nextLine();
                            System.out.print("Playlist name: ");
                            name = console.nextLine();
                            playlist = client.utils.createPlaylistFromFile(user, path, name);

                            client.playlist.removeDuplicate(user.getId(), playlist.getId());

                            System.out.print("Sort by Asc [y|n]?: ");
                            isAsc = console.nextLine().equals("y");

                            client.playlist.sort(user.getId(), playlist.getId(), isAsc);

                            System.out.print("Save path [" + playlist.getName() + "]: ");
                            path = console.nextLine();
                            client.utils.exportPlaylistToFile(user, playlist.getId(), path.equals("") ? playlist.getName() : path);
                        } catch (Exception e) {
                            System.out.print("Error! Save log path[log.txt]: ");
                            path = console.nextLine();
                            Utils.saveErrorLog("run task", path.equals("") ? "log.txt" : path, e);
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