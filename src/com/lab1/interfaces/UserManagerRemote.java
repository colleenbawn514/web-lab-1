package com.lab1.interfaces;

import com.lab1.common.User;
import com.lab1.exception.PlaylistNotFoundException;
import com.lab1.exception.UserNotFoundException;

import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface UserManagerRemote extends Remote {
    User create(String login, String password, String name) throws RemoteException, IllegalArgumentException;

    User get(String login, String password) throws RemoteException, PlaylistNotFoundException, UserNotFoundException, AuthenticationException;

    boolean isExistUser(String login) throws RemoteException, UserNotFoundException;

    Map<Integer, User> getAll() throws RemoteException, PlaylistNotFoundException;
}
