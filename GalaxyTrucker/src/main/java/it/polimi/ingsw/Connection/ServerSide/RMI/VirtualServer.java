package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class VirtualServer extends UnicastRemoteObject implements ServerRemoteInterface{

    private Server centralServer;

    public VirtualServer(Server centralServer) throws RemoteException{

        this.centralServer = centralServer;

    }

    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }

    @Override
    public void registerClient(String ipAddress) throws RemoteException {

        System.out.println(ipAddress + " is connected through rmi protocol");

    }

    @Override
    public void makePlayerJoin(ClientRemoteInterface virtualClient, ClientInfo clientInfo) throws RemoteException {

        Joiner joiner = new Joiner(clientInfo, centralServer, virtualClient);

        try {

            joiner.start();
            virtualClient.setInGame(true);

        } catch (RemoteException e) {

            centralServer.removeNickName(clientInfo.getNickname());
            System.out.println(e.getMessage());
            System.out.println("Client " + clientInfo.getNickname() + " disconnected!");
        }

    }
}
