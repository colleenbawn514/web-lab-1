package com.lab1.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrackRMI extends Remote {
    int getId() throws RemoteException;

    String getArtist() throws RemoteException;

    String getName() throws RemoteException;

    int getDuration() throws RemoteException;

    double getSize() throws RemoteException;

    boolean isEqual(String artist, String name, int duration, double size) throws RemoteException;
}
