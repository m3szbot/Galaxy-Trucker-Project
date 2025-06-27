package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface which defines the remote methods which the server offers
 * to the client when communicating through rmi protocol.
 *
 * @author carlo
 */

public interface ServerRemoteInterface extends Remote {

    /**
     * Registers the client
     * @param ipAddress
     * @throws RemoteException
     */

    public void registerClient(String ipAddress) throws RemoteException;

    /**
     * Make a player enter the lobby and join a game
     * @param virtualClient
     * @param clientInfo
     * @return true if the player joined a game, false otherwise
     * @throws RemoteException
     */

    public boolean makePlayerJoin(ClientRemoteInterface virtualClient, ClientInfo clientInfo) throws RemoteException;

    /**
     * Used to ping the server
     * @return true is the server is alive
     * @throws RemoteException if the server is not reachable and therefore the client is disconnected
     */

    public boolean isAlive() throws RemoteException;

}
