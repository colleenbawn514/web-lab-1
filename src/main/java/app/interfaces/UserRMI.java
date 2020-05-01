package com.lab1.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserRMI extends Remote {
    int getId() throws RemoteException;

    String getLogin() throws RemoteException;

    String getName() throws RemoteException;

    ArrayList<Integer> getPlaylistIds() throws RemoteException;

    boolean equalsPassword(String password) throws RemoteException;
}
