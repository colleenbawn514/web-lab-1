package com.lab1.interfaces;

import com.lab1.common.Track;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrackManagerRMI extends Remote {
    public Track get(int trackId) throws RemoteException;

    public Track create(String artist, String name, double size, int duration) throws RemoteException;
}
