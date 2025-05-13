package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.ClientInfo;
import it.polimi.ingsw.Model.GameInformation.GameType;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface defining all the methods that the client
 * can call when using RMI while communicating with
 * the server
 *
 * @author carlo
 */

public interface RMIJoiner extends Remote {

    /**
     *
     * @return true if nobody is joining the game
     */

    public boolean joinGame() throws RemoteException;

    /**
     *
     * @return true if the player is the first one joining
     */

    public boolean isFirstPlayer() throws RemoteException;

    /**
     *
     * @param nickname
     * @return true if the nickname is repeated
     */

    public boolean isNameRepeated(String nickname) throws RemoteException;

    /**
     * releases the reentrant lock of the server
     */

    public void releaseLock() throws RemoteException;

    /**
     * adds a non-first player to the current game
     * @param clientInfo
     */

    public void addPlayer(ClientInfo clientInfo) throws RemoteException;

    /**
     * adds the first player to the current game
     * @param clientInfo
     * @param gameType
     * @param numberOfPlayers
     */
    public void addPlayer(ClientInfo clientInfo, GameType gameType, int numberOfPlayers) throws RemoteException;

    /**
     *
     * @return the game code
     */

    public int getGameCode() throws RemoteException;

    /**
     *
     * @return the game creator
     */

    public String getCurrentGameCreator() throws RemoteException;
}
