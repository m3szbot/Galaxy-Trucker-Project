package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.CrewType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.*;

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
     * Correct errors and set crew types.
     */
    @Override
    public void run() {
        // correct errors
        try {
            errorCorrector();
        } catch (PlayerDisconnectedException e) {
            // disconnect player and end player thread
            gameMessenger.disconnectPlayer(gameInformation, player);
            return;
        } catch (NoHumanCrewLeftException e) {
            // force player to give up and end player thread
            ExceptionsHandler.handleNoHumanCrewLeftException(gameMessenger, player, gameInformation.getFlightBoard());
            return;
        }

        // errors corrected
        playerMessenger.printMessage("There are no errors in your shipboard.\n");

        // select crew types
        try {
            selectCrewTypes();
        } catch (PlayerDisconnectedException e) {
            return;
        }

        // crew selection finished
        playerMessenger.printShipboard(player.getShipBoard());
        playerMessenger.printMessage("Your ship is all set, please wait for other players.");

        Sleeper.sleepXSeconds(2);
        // end of thread
    }

    /**
     * Handle the error correction of the player's shipboard.
     *
     * @author Boti
     */
    private void errorCorrector() throws PlayerDisconnectedException, NoHumanCrewLeftException {
        ShipBoard shipBoard = player.getShipBoard();
        int[] coordinates;

        // controls
        boolean isErroneous = shipBoard.isErroneous();
        int errorTrials = shipBoard.getErrorCount() + 5;

        // correct errors
        while (isErroneous && errorTrials > 0) {
            // print error messages
            playerMessenger.printShipboard(shipBoard);
            playerMessenger.printMessage(getErrorsMessage(shipBoard));
            playerMessenger.printMessage("Enter column and row of component to remove (col row):");

            // controls
            boolean removeComponentSuccess = false;
            int removeTrials = 5;

            // elaborate player input: removal loop
            while (!removeComponentSuccess && removeTrials > 0) {
                // get player input
                coordinates = playerMessenger.getPlayerCoordinates();
                // throws PlayerDisconnectedException

                // remove component
                try {
                    // flag used to exit/repeat removal loop
                    removeComponentSuccess = true;
                    shipBoard.removeComponent(coordinates[0], coordinates[1], true);
                    // throws PlayerDisconnectedException, NoHumanCrewLeftException

                } catch (IllegalSelectionException e) {
                    // repeat removal loop
                    removeComponentSuccess = false;
                    // print exception message to player
                    playerMessenger.printMessage(e.getMessage());

                } catch (FracturedShipBoardException e) {
                    // handle fractured shipboard
                    ExceptionsHandler.handleFracturedShipBoardException(playerMessenger, e);
                    // throws PlayerDisconnectedException
                }

                // try again if couldn't remove selected component
                removeTrials--;
            }

            // loop while there are still errors
            isErroneous = shipBoard.isErroneous();
            errorTrials--;
        }
        // error correction finished
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

    /**
     * Handle the crew selection of the player's shipboard.
     *
     * @author Boti
     */
    private void selectCrewTypes() throws PlayerDisconnectedException {
        ShipBoard shipBoard = player.getShipBoard();
        ShipBoardAttributes shipBoardAttributes = shipBoard.getShipBoardAttributes();
        String message;
        String inputString;
        CrewType crewType = CrewType.Human;

        Sleeper.sleepXSeconds(2);

        // scan shipboard (only valid cells)
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {

                // select crew type for current component:
                // only ask player if alien could be set on the given component
                if ((shipBoardAttributes.getHumanCrewMembers() > 2) && (shipBoard.getComponentMatrix()[i][j] instanceof Cabin) &&
                        (// check for purple or brown alien conditions (alien not present + support nearby)
                                (!shipBoardAttributes.getAlien(CrewType.Purple) && shipBoard.checkForAlienSupport(i, j, CrewType.Purple)) ||
                                        (!shipBoardAttributes.getAlien(CrewType.Brown) && shipBoard.checkForAlienSupport(i, j, CrewType.Brown))
                        )) {
                    // alien can be selected
                    playerMessenger.printShipboard(shipBoard);

                    // selection flags
                    boolean selectCrewSuccess = false;
                    int selectTrials = 5;

                    // selection loop for current component
                    while (!selectCrewSuccess && selectTrials > 0) {
                        // get player input string
                        message = String.format("Select the crew type for the cabin at %d %d (Human/Purple/Brown): ", i + 1, j + 1);
                        playerMessenger.printMessage(message);
                        inputString = playerMessenger.getPlayerString();
                        // throws PlayerDisconnectedException

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

                        // try to set crew type if successful selection
                        if (selectCrewSuccess) {
                            try {
                                shipBoard.setCrewType(i + 1, j + 1, crewType);
                            } catch (IllegalSelectionException e) {
                                // failed to set crew (alien not supported etc.)
                                playerMessenger.printMessage(e.getMessage());
                                selectCrewSuccess = false;
                            }
                        }

                        // loop until crew is correctly set for the current cabin or trials exhausted
                        selectTrials--;
                    }

                    // selection loop ended for current component
                    if (!selectCrewSuccess) {
                        playerMessenger.printMessage("Crew couldn't be changed for current component.");
                    }

                }
                // iterate to next component
            }
        }
        // crew selection finished
    }


}
