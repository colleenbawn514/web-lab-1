package app.interfaces;
import app.common.Playlist;
import app.common.Track;
import app.exception.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface PlaylistManagerRemote extends Remote {
    Playlist create(int userId, String name) throws RemoteException, IllegalArgumentException, UserNotFoundException;

    Playlist get(int userId, int playlistId) throws RemoteException, PlaylistNotFoundException, UserNotFoundException;

    Map<Integer, String> getAll(int userId) throws RemoteException, PlaylistNotFoundException, UserNotFoundException;

    void addTrack(int userId, int playlistId, Track track) throws RemoteException, PlaylistNotFoundException, UserNotFoundException;

    void sort(int userId, int playlistId, boolean isAsc) throws RemoteException, PlaylistNotFoundException, UserNotFoundException;

   // void removeDuplicate(int userId, int playlistId) throws RemoteException, PlaylistNotFoundException, TrackNotFoundException, UserNotFoundException;
}
