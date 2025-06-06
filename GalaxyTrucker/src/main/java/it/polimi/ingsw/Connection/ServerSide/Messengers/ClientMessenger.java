package it.polimi.ingsw.Connection.ServerSide.Messengers;

import it.polimi.ingsw.Connection.ServerSide.Server;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains all the gameMessenger. For each game there is
 * a gameMessenger associated with it, which is used to communicate with
 * the client
 *
 * @author carlo
 */

public class ClientMessenger {

    private static Map<Integer, GameMessenger> gameMessengerMap = new HashMap<>();
    private static Server centralServer;

    /**
     * when a new game is started, addGame must be called to add the gameMessenger for
     * the game
     *
     * @param gameCode of the added game
     */

    public static void addGame(int gameCode) {

        gameMessengerMap.put(gameCode, new GameMessenger());

    }

    public static Server getCentralServer() {
        return centralServer;
    }

    public static void setCentralServer(Server centralServer) {
        ClientMessenger.centralServer = centralServer;
    }

    /**
     * @param gameCode
     * @return gameMessenger of the current game
     */

    public static GameMessenger getGameMessenger(int gameCode) {
        return gameMessengerMap.get(gameCode);
    }

    public static void endGame(int gameCode) {
        gameMessengerMap.remove(gameCode);
    }

}
