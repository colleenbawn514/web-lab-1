package com.lab1.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PlaylistRMI extends Remote {
    public int getId();

    public int getCountsTracks() throws RemoteException;

    public ArrayList<Integer> getTrackIds() throws RemoteException;

    public String getName() throws RemoteException;

}
