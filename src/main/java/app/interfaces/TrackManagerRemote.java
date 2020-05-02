package app.interfaces;
import app.common.Track;
import app.exception.TrackNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrackManagerRemote extends Remote {
    public Track get(int trackId) throws RemoteException, TrackNotFoundException;

    public Track create(String author, String name, double size, int duration) throws RemoteException, IllegalArgumentException, TrackNotFoundException;
}
