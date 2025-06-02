package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.FracturedShipBoardException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.List;

/**
 * Controller class to handle FracturedShipBoardException.
 */
public class FracturedShipBoardHandler extends Phase {
    PlayerMessenger playerMessenger;
    Player player;
    List<ShipBoard> shipBoardsList;


    public FracturedShipBoardHandler(GameInformation gameInformation, PlayerMessenger playerMessenger, FracturedShipBoardException exception) {
        super(gameInformation);
        this.playerMessenger = playerMessenger;
        this.player = playerMessenger.getPlayer();
        this.shipBoardsList = exception.getShipBoardsList();
    }

    @Override
    public void start() {
        playerMessenger.printMessage("Your shipboard has broken into multiple parts:\n");

        // print shipboards
        for (int i = 0; i < shipBoardsList.size(); i++) {
            playerMessenger.printMessage(String.format("Possible shipboard %d:\n", i));
            playerMessenger.printShipboard(shipBoardsList.get(i));
        }

        // select shipboard
        boolean selectionSuccess = false;
        int trials = 5;
        int selected;

        while (!selectionSuccess && trials > 0) {
            playerMessenger.printMessage("Please select the part to keep (enter number):");
            // get player input
            try {
                selected = playerMessenger.getPlayerInt();
            } catch (PlayerDisconnectedException e) {
                // TODO handle disconnection in caller controller
                // disconnect player
                gameMessenger.disconnectPlayer(gameInformation, player);
                // end thread
                return;
            }

            // select shipboard
            try {
                player.setShipBoard(shipBoardsList.get(selected));
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
