package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.GameMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

public class CorrectionThread implements Runnable {
    final GameInformation gameInformation;
    final Player player;
    final GameMessenger gameMessenger;
    final PlayerMessenger playerMessenger;

    public CorrectionThread(GameInformation gameInformation, Player player) {
        this.gameInformation = gameInformation;
        this.player = player;
        this.gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());
        this.playerMessenger = gameMessenger.getPlayerMessenger(player);
    }

    /**
     * remove components until shipboard of player is valid
     */
    @Override
    public void run() {
        boolean errors;
        StringBuilder message = new StringBuilder();
        int[] coordinates;
        ShipBoard shipBoard = player.getShipBoard();
        // check if there are errors
        errors = shipBoard.isErroneous();
        // correct errors
        while (errors) {
            // construct errors message
            message.append("There are errors in your ship, please correct them:\n");
            for (int i = 0; i < shipBoard.getMatrixCols(); i++) {
                for (int j = 0; j < shipBoard.getMatrixRows(); j++) {
                    if (shipBoard.getMatrErrors()[i][j]) {
                        message.append("Error in: %d %d\n", i + 1, j + 1);
                    }
                }
            }
            message.append("Enter column and row (col row):");
            playerMessenger.printMessage(message.toString());

            try {
                coordinates = playerMessenger.getPlayerCoordinates();
            } catch (PlayerDisconnectedException e) {
                // handle disconnected player
                gameMessenger.disconnectPlayer(gameInformation, player);
                return;
            }

            // no check for col, row value - if out of bounds, nothing happens
            // trigger automatically removes disconnected components - set to false
            shipBoard.removeComponent(coordinates[0], coordinates[1], false);
            errors = shipBoard.isErroneous();
        }
        // errors corrected
        playerMessenger.printMessage("Your ship is valid, please wait for other players.");
        // end of thread
    }
}
