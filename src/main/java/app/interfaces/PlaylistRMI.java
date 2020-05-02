package app.interfaces;
import app.common.Track;
import app.exception.PlaylistNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface PlaylistRMI<T> extends Remote {
    public int getId();

    public String getName() throws RemoteException;

    public int getSize() throws RemoteException;

    public ArrayList<T> getTrackIds() throws RemoteException;
}
