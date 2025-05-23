package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemoteInterface extends Remote {

    public void registerClient(String ipAddress, ClientRemoteInterface virtualClient) throws RemoteException;

}
