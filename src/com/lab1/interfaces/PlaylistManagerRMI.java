package com.lab1.interfaces;
import com.lab1.common.Playlist;
import com.lab1.common.Track;
import com.lab1.exception.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface PlaylistManagerRMI extends Remote {
    public Playlist get(int playlistId) throws RemoteException, PlaylistNotFoundException;

    public ArrayList<Integer> getAllIds() throws RemoteException;

    public Map<Integer, Track> getTracks(int playlistId) throws RemoteException, PlaylistNotFoundException;

    public Playlist create(String name) throws RemoteException, IllegalArgumentException;

    public void sort(int playlistId, boolean isAsc) throws RemoteException, PlaylistNotFoundException;

    public void addTrack(int playlistId, Track track) throws RemoteException, PlaylistNotFoundException;

    public void removeDuplicate(int playlistId) throws RemoteException, PlaylistNotFoundException;
}
