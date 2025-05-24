package it.polimi.ingsw.Connection.ServerSide.messengers;

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

    /**
     * when a new game is started, addGame must be called to add the gameMessenger for
     * the game
     * @param gameCode of the added game
     */

    public static void addGame(int gameCode){

        gameMessengerMap.put(gameCode, new GameMessenger());

    }

    /**
     * @param gameCode
     * @return gameMessenger of the current game
     */

    public static GameMessenger getGameMessenger(int gameCode){
        return gameMessengerMap.get(gameCode);
    }

}
