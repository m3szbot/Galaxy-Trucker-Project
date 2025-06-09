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

    private FracturedShipBoardHandler() {
        throw new UnsupportedOperationException();
    }

    public static void handleFracture(PlayerMessenger playerMessenger, FracturedShipBoardException exception) throws PlayerDisconnectedException, NoHumanCrewLeftException {
        Player player = playerMessenger.getPlayer();
        List<ShipBoard> shipBoardsList = exception.getShipBoardsList();

        playerMessenger.printMessage("Your shipboard has broken into multiple parts:");

        // print shipboards
        for (int i = 0; i < shipBoardsList.size(); i++) {
            playerMessenger.printMessage("\n###############################################################################");
            playerMessenger.printMessage(String.format("Possible shipboard %d:", i));
            playerMessenger.printShipboard(shipBoardsList.get(i));
        }

        // select shipboard
        boolean selectionSuccess = false;
        int trials = 5;
        int selected;

        while (!selectionSuccess && trials > 0) {
            playerMessenger.printMessage("Please select the part to keep (enter number):");
            // get player input
            selected = playerMessenger.getPlayerInt();
            // if PlayerDisconnectedException is thrown, propagate it to the caller Controller

            // TODO
            // select shipboard
            try {
                // erase all other possible shipboard
                for (ShipBoard shipBoard : shipBoardsList) {
                    if (!shipBoard.equals(shipBoardsList.get(selected))) {
                        player.getShipBoard().eraseShipboardFromRealShipboard(shipBoard);
                    }
                }

                // set only if no exception thrown
                selectionSuccess = true;
            } catch (IndexOutOfBoundsException e) {
                // invalid selection
                playerMessenger.printMessage("Please enter a valid shipboard number!");
            }

            // loop again if selection not successful
            trials--;
        }

        playerMessenger.printMessage("You have successfully selected your new shipboard:");
        playerMessenger.printShipboard(player.getShipBoard());

    }
}
