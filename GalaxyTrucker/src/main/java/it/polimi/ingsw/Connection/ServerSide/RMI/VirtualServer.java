package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Connection.ServerSide.utils.GameJoinerThread;

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

        if(centralServer.checkNickname(clientInfo.getNickname())){

            virtualClient.printPreJoinMessage("A player connected to the server already has your nickname, please reconnect using a new one");
            return;

        }
        else{
            (new GameJoinerThread(centralServer, virtualClient, clientInfo.getNickname())).start();
        }

        virtualClient.setInGame(true);

    }

}
