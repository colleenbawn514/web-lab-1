package com.lab1.interfaces;
import com.lab1.common.Playlist;
import com.lab1.common.Track;
import com.lab1.exception.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface PlaylistManagerRemote extends Remote {
    Playlist create(String name) throws RemoteException, IllegalArgumentException;

    Playlist get(int playlistId) throws RemoteException, PlaylistNotFoundException;

    Map<Integer, String> getAll() throws RemoteException, PlaylistNotFoundException;

    void addTrack(int playlistId, Track track) throws RemoteException, PlaylistNotFoundException;

    void sort(int playlistId, boolean isAsc) throws RemoteException, PlaylistNotFoundException;

    void removeDuplicate(int playlistId) throws RemoteException, PlaylistNotFoundException, TrackNotFoundException;
}
