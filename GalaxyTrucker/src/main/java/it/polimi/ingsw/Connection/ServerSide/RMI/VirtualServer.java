package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Connection.ServerSide.utils.GameJoinerThread;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Remote class which offers remote methods that are invoked by the client when
 * connecting to the server through rmi protocol
 *
 * @author carlo
 */

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

    /**
     *
     * @param virtualClient
     * @param clientInfo
     * @return true if the joining phase has been done correctly, false otherwise
     * @throws RemoteException
     */

    @Override
    public boolean makePlayerJoin(ClientRemoteInterface virtualClient, ClientInfo clientInfo) throws RemoteException {

        GameJoinerThread gameJoinerThread = new GameJoinerThread(centralServer, virtualClient, clientInfo.getNickname());

        if(centralServer.checkNickname(clientInfo.getNickname())){

            virtualClient.printPreJoinMessage("A player connected to the server already has your nickname, please reconnect using a new one");
            return false;

        }
        else{

            gameJoinerThread.start();
        }

        try {

            gameJoinerThread.join();

        }catch (InterruptedException e){
            System.err.println("Game joiner thread was abnormally interrupted");
        }

        virtualClient.setInGame(true);

        if(virtualClient.getCommand() != null){
            if(virtualClient.getCommand().equals("kicked")){
                return false;
            }
        }

        return true;

    }

}
