package com.lab1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MusicLibrary extends Remote {
    //Создает новый плейлист и возращет его ID
    int createPlaylist(String name) throws RemoteException;

    //Создает новый плейлист и возращет его ID
    int createPlaylistFromFile(String name, String path) throws RemoteException;

    //Добавляет трек в библиотеку и возвращает его ID
    int addTrack(int playlistId, String name, double size, int duration) throws RemoteException;

    //Удаление трека
    void removeTrack(int playlistId, int trackId) throws RemoteException;

    //Возвращает имя трек по ID
    String getTrackNameById(int playlistId, int trackId) throws RemoteException;

    //Возвращает размер трек по ID
    double getTrackSizeById(int playlistId, int trackId) throws RemoteException;

    //Возвращает длительность трек по ID
    int getTrackDurationById(int playlistId, int trackId) throws RemoteException;

    //Возвращает плейлисты
    int[] getPlaylistIds() throws RemoteException;

    //Возвращает имя плейлиста по ID
    String getPlaylistNameById(int playlistId) throws RemoteException;

    //Возвращает список треков плейлиста по ID
    int[] getPlaylistTrackIdsById(int playlistId) throws RemoteException;

    //Сортирует плейлист
    void sortPlaylist (int playlistId, boolean isAsc) throws RemoteException;
}