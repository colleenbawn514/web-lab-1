package com.lab1.interfaces;
import com.lab1.common.Track;
import com.lab1.exception.PlaylistNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface PlaylistRMI extends Remote {
    public int getId();

    public String getName() throws RemoteException;

    public int getSize() throws RemoteException;

    public ArrayList<Integer> getTrackIds() throws RemoteException;
}
