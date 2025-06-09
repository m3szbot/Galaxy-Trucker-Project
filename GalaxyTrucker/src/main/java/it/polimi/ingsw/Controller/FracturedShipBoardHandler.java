package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.ShipBoard.FracturedShipBoardException;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.List;

/**
 * Controller class used to handle FracturedShipBoardException.
 * Utility/helper class, cannot have instances:
 * final class, private constructor, static methods.
 *
 * @author Boti
 */
public final class FracturedShipBoardHandler {
    // Utility class, cannot be instantiated
    private FracturedShipBoardHandler() {
        throw new UnsupportedOperationException();
    }

    public static void handleFracture(PlayerMessenger playerMessenger, FracturedShipBoardException exception) throws PlayerDisconnectedException, NoHumanCrewLeftException {
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

        // end of fracture handling
    }
}
