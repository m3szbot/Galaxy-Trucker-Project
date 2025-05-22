package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.VirtualClient;
import it.polimi.ingsw.Connection.ServerSide.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class VirtualServer extends UnicastRemoteObject implements ServerRemoteInterface{

    private Server centralServer;

    public VirtualServer(Server centralServer) throws RemoteException{

        this.centralServer = centralServer;

    }

    @Override
    public void registerClient(String ipAddress, VirtualClient virtualClient) throws RemoteException {

        System.out.println(ipAddress + "is connected through rmi protocol");
        virtualClient.makeClientJoin(centralServer);

    }
}
