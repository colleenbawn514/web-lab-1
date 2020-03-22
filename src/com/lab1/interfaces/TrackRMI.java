package com.lab1.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrackRMI extends Remote {
    public String getName() throws RemoteException;

    public String getArtist() throws RemoteException;

    public double getSize() throws RemoteException;

    public int getId() throws RemoteException;

    public int getDuration() throws RemoteException;

    public boolean isEqual(String artist, String name, double size, int duration) throws RemoteException;
}
