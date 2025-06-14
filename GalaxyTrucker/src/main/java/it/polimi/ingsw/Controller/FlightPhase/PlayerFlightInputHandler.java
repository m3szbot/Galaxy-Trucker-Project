package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Static class which handles the input thread of the flightPhase
 *
 * @author carlo
 */

public class PlayerFlightInputHandler {

    private static Map<Player, InputThread> playerInputThreadMap = new HashMap<>();

    /**
     *
     * @param player
     * @return true if the input thread of the related player is present, false otherwise.
     */

    public static boolean checkInputThreadActivity(Player player){
       if(playerInputThreadMap.containsKey(player)){
           return true;
       }
       else {
           return false;
       }
    }

    /**
     * adds a new player to the handler memory. The player input thread is also
     * started
     * @param player
     * @param gameInformation
     */

    public static void addPlayer(Player player, GameInformation gameInformation){

        InputThread inputThread = new InputThread(player, gameInformation);
        playerInputThreadMap.put(player, inputThread);
        inputThread.start();
    }

    /**
     * Stops the inputThread of the player. To call if a disconnection occur or if the flight
     * phase has ended.
     * @param player
     */

    public static void removePlayer(Player player){

        InputThread inputThread = playerInputThreadMap.get(player);
        inputThread.endThread();
        playerInputThreadMap.remove(player);

    }

    /**
     * To be called when the turn of the player has started
     * @param player
     */

    public static void startPlayerTurn(Player player){
       InputThread inputThread = playerInputThreadMap.get(player);
       inputThread.setPlayerTurn(true);
    }

    /**
     * To be called when the player turn ends
     * @param player
     */

    public static void endPlayerTurn(Player player){
        InputThread inputThread = playerInputThreadMap.get(player);
        inputThread.setPlayerTurn(false);
    }

}
