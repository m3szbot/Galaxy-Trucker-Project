package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.ClientInfo;
import it.polimi.ingsw.Model.GameInformation.GameType;

import java.rmi.Remote;

/**
 * Interface defining all the methods that the client
 * can call when using RMI while comunicating with
 * the server
 *
 * @author carlo
 */

public interface RMIJoiner extends Remote {

    /**
     *
     * @return true if nobody is joining the game
     */

    public boolean joinGame();

    /**
     *
     * @return true if the player is the first one joining
     */

    public boolean isFirstPlayer();

    /**
     *
     * @param nickname
     * @return true if the nickname is repeated
     */

    public boolean isNameRepeated(String nickname);

    /**
     * releases the reentrant lock of the server
     */

    public void releaseLock();

    /**
     * adds a non-first player to the current game
     * @param clientInfo
     */

    public void addPlayer(ClientInfo clientInfo);

    /**
     * adds the first player to the current game
     * @param clientInfo
     * @param gameType
     * @param numberOfPlayers
     */
    public void addPlayer(ClientInfo clientInfo, GameType gameType, int numberOfPlayers);

    /**
     *
     * @return the game code
     */

    public int getGameCode();

    /**
     *
     * @return the game creator
     */

    public String getCurrentGameCreator();
}
