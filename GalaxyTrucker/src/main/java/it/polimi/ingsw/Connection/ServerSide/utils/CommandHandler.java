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
     * @param command entered by the player
     * @param playerMessenger of the player
     * @return 'repeat' or 'unblocked'
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
            case "refresh-shipboard" -> {
                return (new ShipBoardRefresher()) .start(playerMessenger);
            }
            case "fractured-shipboard" -> {

                return (new FracturedShipboardCommand()).showFracturedShipboard(playerMessenger);

            }

            default -> {
                return "repeat";
            }
        }
    }

}
