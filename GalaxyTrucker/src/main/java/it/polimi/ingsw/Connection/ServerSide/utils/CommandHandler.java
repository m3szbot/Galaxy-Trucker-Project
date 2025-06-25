package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;

/**
 * Static class responsible for handling the player commands sent to the server.
 *
 * @author carlo
 */

public final class CommandHandler {

    /**
     *
     * @param command
     * @param playerMessenger
     * @return true if the command was correct and executed
     * @throws PlayerDisconnectedException
     */
    public static String executeCommand(String command, PlayerMessenger playerMessenger) throws PlayerDisconnectedException {
        switch (command){
            case "show-shipboard" ->{
                return (new FoeShipBoardPrinter(playerMessenger)).start();
            }
            case "private-message"->{

                return (new ChatUtil(playerMessenger)).startPrivateMessageHandler();

            }
            case "public-message"->{
                return (new ChatUtil(playerMessenger)).startPublicMessageHandler();
            }
            case "refresh" -> {
                return (new ShipBoardRefresher()) .start(playerMessenger);
            }
            default -> {
                return "repeat";
            }
        }
    }

}
