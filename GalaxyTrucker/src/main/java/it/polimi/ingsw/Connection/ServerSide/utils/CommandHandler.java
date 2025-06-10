package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;

/**
 * Static class responsible for handling the player commands sent to the server.
 *
 * @author carlo
 */

public class CommandHandler {

    /**
     *
     * @param command
     * @param playerMessenger
     * @return true if the command was correct and executed
     * @throws PlayerDisconnectedException
     */
    public static boolean executeCommand(String command, PlayerMessenger playerMessenger) throws PlayerDisconnectedException {
        switch (command){
            case "show-shipboard" ->{
                (new FoeShipBoardPrinter(playerMessenger)).start();
                return true;
            }
            case "private-message"->{

                (new ChatUtil(playerMessenger)).startPrivateMessageHandler();
                return true;

            }
            case "public-message"->{
                (new ChatUtil(playerMessenger)).startPublicMessageHandler();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

}
