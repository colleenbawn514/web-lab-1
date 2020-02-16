package com.lab1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MusicLibrary extends Remote {
    //Добавляет трек в библиотеку
    void addTrack(String name, double size, int duration) throws RemoteException;

    //Возвращает имя трек по ID
    String getTrackNameById(int trackId) throws RemoteException;

    //Возвращает размер трек по ID
    double getTrackSizeById(int trackId) throws RemoteException;

    //Возвращает длительность трек по ID
    int getTrackDurationById(int trackId) throws RemoteException;

    //Создает новый плейлист
    int createPlaylist(String name) throws RemoteException;

    //Возвращает плейлисты
    int[] getPlaylistsIds() throws RemoteException;

    //Добавляет трек в библиотеку
    void addTrackToPlaylist(int playlistId, int trackId) throws RemoteException;

    //Возвращает имя плейлиста по ID
    String getPlaylistNameById(int playlistId) throws RemoteException;

    //Возвращает список треков плейлиста по ID
    int[] getPlaylistById(int playlistId) throws RemoteException;
}