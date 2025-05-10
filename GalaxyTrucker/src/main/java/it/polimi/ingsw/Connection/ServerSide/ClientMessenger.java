package it.polimi.ingsw.Connection.ServerSide;

import java.util.HashMap;
import java.util.Map;

public class ClientMessenger {

    private static Map<Integer, GameMessenger> gameMessengerMap = new HashMap<>();

    public static void addGame(int gameCode){

        gameMessengerMap.put(gameCode, new GameMessenger());

    }

    public static GameMessenger getGameMessenger(int gameCode){
        return gameMessengerMap.get(gameCode);
    }

}
