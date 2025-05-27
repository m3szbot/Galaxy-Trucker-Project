package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemoteInterface extends Remote {

    public void registerClient(String ipAddress) throws RemoteException;

    public void makePlayerJoin(ClientRemoteInterface virtualClient, ClientInfo clientInfo) throws RemoteException;

    public boolean isAlive() throws RemoteException;

}
