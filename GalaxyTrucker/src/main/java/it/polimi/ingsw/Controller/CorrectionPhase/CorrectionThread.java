package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.CrewType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoardAttributes;

import static it.polimi.ingsw.Model.ShipBoard.ShipBoard.*;

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
        ShipBoard shipBoard = player.getShipBoard();
        ShipBoardAttributes shipBoardAttributes = shipBoard.getShipBoardAttributes();
        String message;
        int[] coordinates;
        String inputString;

        // CORRECT ERRORS
        // check if there are errors
        boolean errors = shipBoard.isErroneous();
        boolean removeComponentSuccess = false;

        // correct errors
        while (errors) {
            // print error messages
            playerMessenger.printShipboard(shipBoard);
            message = getErrorsMessage(shipBoard);
            playerMessenger.printMessage(message);
            message = "Enter column and row (col row):";
            playerMessenger.printMessage(message);

            // elaborate player input
            while (!removeComponentSuccess) {
                // get player input and remove component
                try {
                    coordinates = playerMessenger.getPlayerCoordinates();
                    shipBoard.removeComponent(coordinates[0], coordinates[1], false);
                    removeComponentSuccess = true;
                } catch (PlayerDisconnectedException e) {
                    // handle disconnected player
                    gameMessenger.disconnectPlayer(gameInformation, player);
                    return;
                } catch (IllegalArgumentException e) {
                    // print exception message to player
                    playerMessenger.printMessage(e.getMessage());
                    removeComponentSuccess = false;
                }
            }

            // loop while there are still errors
            errors = shipBoard.isErroneous();
        }

        // errors corrected
        message = "There are no errors in your shipboard.\n";
        playerMessenger.printMessage(message);

        // SET CREW TYPE FOR CABINS
        CrewType crewType = CrewType.Human;
        boolean selectCrewSuccess = false;

        playerMessenger.printShipboard(shipBoard);

        // scan shipboard (only valid cells)
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {

                // only ask player if alien could be set on the given component
                if ((shipBoardAttributes.getHumanCrewMembers() > 2) && (shipBoard.getComponentMatrix()[i][j] instanceof Cabin) &&
                        (// check for purple or brown alien conditions (alien not present + support nearby)
                                (!shipBoardAttributes.getAlien(CrewType.Purple) && shipBoard.checkForAlienSupport(i, j, CrewType.Purple)) ||
                                        (!shipBoardAttributes.getAlien(CrewType.Brown) && shipBoard.checkForAlienSupport(i, j, CrewType.Brown))
                        )) {

                    // elaborate player input
                    while (!selectCrewSuccess) {
                        // get player input string
                        message = String.format("Select the crew type for the cabin at %d %d (Human/Purple/Brown): ", i + 1, j + 1);
                        playerMessenger.printMessage(message);

                        try {
                            inputString = playerMessenger.getPlayerString();
                        } catch (PlayerDisconnectedException e) {
                            // handle disconnected player
                            gameMessenger.disconnectPlayer(gameInformation, player);
                            return;
                        }

                        // cast player input
                        switch (inputString.toLowerCase()) {
                            case "human" -> {
                                crewType = CrewType.Human;
                                selectCrewSuccess = true;
                            }
                            case "purple" -> {
                                crewType = CrewType.Purple;
                                selectCrewSuccess = true;
                            }
                            case "brown" -> {
                                crewType = CrewType.Brown;
                                selectCrewSuccess = true;
                            }
                            // invalid input
                            default -> {
                                playerMessenger.printMessage("Please enter a valid input!");
                            }
                        }
                        // set crew type if successfully selected
                        if (selectCrewSuccess) {
                            try {
                                shipBoard.setCrewType(i + 1, j + 1, crewType);
                            } catch (IllegalArgumentException e) {
                                playerMessenger.printMessage(e.getMessage());
                                selectCrewSuccess = false;
                            }
                        }
                        // loop until crew is correctly set for the current cabin
                    }
                }
                // iterate to next component
            }
        }
        // crew selection finished

        playerMessenger.printMessage("Your ship is all set, please wait for other players.");
        // end of thread
    }


    /**
     * Return a string listing the coordinates of the components with errors.
     */
    private String getErrorsMessage(ShipBoard shipBoard) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("There are errors in your ship, please correct them:\n");
        // scan shipboard (only valid cells)
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                if (shipBoard.getErrorsMatrix()[i][j]) {
                    messageBuilder.append(String.format("Error in: %d %d\n", i + 1, j + 1));
                }
            }
        }
        return messageBuilder.toString();
    }
}
