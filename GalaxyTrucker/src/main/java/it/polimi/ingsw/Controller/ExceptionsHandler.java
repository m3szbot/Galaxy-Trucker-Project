package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.ShipBoard.FracturedShipBoardException;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.List;

/**
 * Controller class used to handle Exceptions.
 * Utility/helper class, cannot have instances:
 * final class, private constructor, static methods.
 *
 * @author Boti
 */
public final class ExceptionsHandler {
    // Utility class, cannot be instantiated
    private ExceptionsHandler() {
        throw new UnsupportedOperationException();
    }

    /**
     * Handle NoHumanCrewLeftException.
     * Print messages and remove player from the flightBoard.
     *
     * @param gameMessenger
     */
    public static void handleNoHumanCrewLeftException(GameMessenger gameMessenger, Player player, FlightBoard flightBoard) {
        PlayerMessenger playerMessenger = gameMessenger.getPlayerMessenger(player);

        if (playerMessenger != null) {
            // print player messages
            playerMessenger.printMessage("\nYou have lost all human crew and have been eliminated from the flight.");
            playerMessenger.printMessage("You are now spectating.");
            playerMessenger.printMessage("(your shipboard will be evaluated after the flight ends)");
        }

        // notify all
        gameMessenger.sendMessageToAll(String.format("\n%s has lost all human crew and have been eliminated from the flight.", player.getColouredNickName()));
        gameMessenger.sendMessageToAll("(all shipboards will be evaluated after the flight ends)");

        // remove player from flightboard
        flightBoard.eliminatePlayer(player);

        Sleeper.sleepXSeconds(4);
    }

    /**
     * Handle LappedPlayersException.
     * Print messages and remove lapped players from the flightBoard.
     *
     * @param gameMessenger
     * @param exception
     * @author Boti
     */
    public static void handleLappedPlayersException(GameMessenger gameMessenger, LappedPlayersException exception) {
        // remove lapped players
        FlightBoard flightBoard = exception.getFlightBoard();
        List<Player> playerList = exception.getPlayerList();

        // eliminate players
        for (Player player : playerList) {
            gameMessenger.getPlayerMessenger(player).printMessage("\nYou have been lapped and eliminated from the flight.");
            gameMessenger.getPlayerMessenger(player).printMessage("You are now spectating.");
            gameMessenger.getPlayerMessenger(player).printMessage("(your shipboard will be evaluated after the flight ends)");
            flightBoard.eliminatePlayer(player);
        }

        // notify all
        gameMessenger.sendMessageToAll("");
        for (Player player : playerList)
            gameMessenger.sendMessageToAll(String.format("%s has been lapped and eliminated from the flight.\n", player.getColouredNickName()));
        gameMessenger.sendMessageToAll("(all shipboards will be evaluated after the flight ends)\n");
        Sleeper.sleepXSeconds(4);
    }


    /**
     * Handle FracturedShipboardException.
     *
     * @param playerMessenger
     * @param exception
     * @throws PlayerDisconnectedException
     * @throws NoHumanCrewLeftException
     * @author Boti
     */
    public static void handleFracturedShipBoardException(PlayerMessenger playerMessenger, FracturedShipBoardException exception) throws PlayerDisconnectedException, NoHumanCrewLeftException {
        Player player = playerMessenger.getPlayer();
        List<ShipBoard> validShipBoardsList = exception.getValidShipBoardsList();

        playerMessenger.printMessage("Your shipboard has broken into multiple parts:");

        // print possible shipboards
        for (int i = 0; i < validShipBoardsList.size(); i++) {
            playerMessenger.printMessage("\n###############################################################################");
            playerMessenger.printMessage(String.format("Possible shipboard #%d:", i));
            playerMessenger.printShipboard(validShipBoardsList.get(i));
        }

        // select shipboard variables
        boolean selectionSuccess = false;
        int trials = 5;
        int selected = 0;

        // select shipboard
        while (!selectionSuccess && trials > 0) {
            playerMessenger.printMessage("Please select the part to keep (enter number):");
            // get player input
            selected = playerMessenger.getPlayerInt();
            // if PlayerDisconnectedException is thrown, propagate it to the caller Controller

            // successful selection
            if (selected >= 0 && selected < validShipBoardsList.size()) {
                selectionSuccess = true;
            } else {
                playerMessenger.printMessage("Please enter a valid shipboard number!");
            }
            // repeat if selection not successful
            trials--;
        }

        // if selection not successful, keep shipboard number 0 automatically
        if (!selectionSuccess) {
            selected = 0;
            playerMessenger.printMessage("Failed to select shipboard, shipboard #0 has been automatically selected.");
        }

        // erase not selected shipboards
        for (int i = 0; i < validShipBoardsList.size(); i++) {
            if (i != selected) {
                player.getShipBoard().eraseShipboardFromRealShipboard(validShipBoardsList.get(i));
            }
        }

        // shipboard to keep successfully selected (others erased)
        playerMessenger.printMessage("You have successfully selected your new shipboard:");
        playerMessenger.printShipboard(player.getShipBoard());
        Sleeper.sleepXSeconds(4);

        // end of fracture handling
    }
}
