package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.FracturedShipBoardHandler;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.CrewType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
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
        if (errorCorrector()) {
            // end thread
            return;
        }
        // errors corrected
        playerMessenger.printMessage("There are no errors in your shipboard.\n");

        // select crew types
        if (selectCrewTypes()) {
            // end thread
            return;
        }
        // crew selection finished
        playerMessenger.printShipboard(player.getShipBoard());
        playerMessenger.printMessage("Your ship is all set, please wait for other players.");
        // end of thread
    }

    /**
     * Handle the error correction of the player's shipboard. Returns true if the player's thread should be ended.
     *
     * @return true if the player's thread should be ended, false if not.
     * @author Boti
     */
    private boolean errorCorrector() {
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

            // elaborate player input
            while (!removeComponentSuccess && removeTrials > 0) {
                // get player input
                try {
                    coordinates = playerMessenger.getPlayerCoordinates();
                } catch (Exception e) {
                    // handle disconnected player
                    e.printStackTrace();
                    gameMessenger.disconnectPlayer(gameInformation, player);
                    // end thread
                    return true;
                }

                // remove componentnioninini
                try {
                    // TODO enable fracture trigger
                    shipBoard.removeComponent(coordinates[0], coordinates[1], false);
                    // set only if no exceptions thrown
                    removeComponentSuccess = true;

                } catch (IllegalArgumentException e) {
                    // print exception message to player
                    playerMessenger.printMessage(e.getMessage());

                } catch (NoHumanCrewLeftException e) {
                    // eliminate player
                    playerMessenger.printMessage(e.getMessage());
                    gameInformation.removePlayers(player);
                    // end thread
                    return true;

                } catch (FracturedShipBoardException e) {
                    // TODO delete print
                    System.out.println("Shipboard fractured!");
                    // handle fractured shipboard
                    FracturedShipBoardHandler handler = new FracturedShipBoardHandler(gameInformation, playerMessenger, e);
                    handler.start();
                }

                // try again if couldn't remove selected component
                removeTrials--;
            }

            // loop while there are still errors
            isErroneous = shipBoard.isErroneous();
            errorTrials--;
        }
        // error correction finished
        // do not end thread
        return false;
    }

    /**
     * Handle the crew selection of the player's shipboard. Returns true if the player's thread should be ended.
     *
     * @return true if the player's thread should be ended, false if not.
     * @author Boti
     */
    private boolean selectCrewTypes() {
        ShipBoard shipBoard = player.getShipBoard();
        ShipBoardAttributes shipBoardAttributes = shipBoard.getShipBoardAttributes();
        String message;
        String inputString;
        CrewType crewType = CrewType.Human;
        boolean selectCrewSuccess = false;

        // scan shipboard (only valid cells)
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {

                // only ask player if alien could be set on the given component
                if ((shipBoardAttributes.getHumanCrewMembers() > 2) && (shipBoard.getComponentMatrix()[i][j] instanceof Cabin) &&
                        (// check for purple or brown alien conditions (alien not present + support nearby)
                                (!shipBoardAttributes.getAlien(CrewType.Purple) && shipBoard.checkForAlienSupport(i, j, CrewType.Purple)) ||
                                        (!shipBoardAttributes.getAlien(CrewType.Brown) && shipBoard.checkForAlienSupport(i, j, CrewType.Brown))
                        )) {
                    // alien could be selected
                    playerMessenger.printShipboard(shipBoard);

                    // elaborate player input
                    int selectTrials = 5;
                    while (!selectCrewSuccess && selectTrials > 0) {
                        // get player input string
                        message = String.format("Select the crew type for the cabin at %d %d (Human/Purple/Brown): ", i + 1, j + 1);
                        playerMessenger.printMessage(message);

                        try {
                            inputString = playerMessenger.getPlayerString();
                        } catch (PlayerDisconnectedException e) {
                            // handle disconnected player
                            gameMessenger.disconnectPlayer(gameInformation, player);
                            // end player thread
                            return true;
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

                        // set crew type if successfully cast
                        if (selectCrewSuccess) {
                            try {
                                shipBoard.setCrewType(i + 1, j + 1, crewType);
                            } catch (IllegalArgumentException e) {
                                // failed to set crew
                                playerMessenger.printMessage(e.getMessage());
                                selectCrewSuccess = false;
                            }
                        }
                        // loop until crew is correctly set for the current cabin
                        selectTrials--;
                    }
                }
                // iterate to next component
            }
        }
        // crew selection finished
        // do not end player thread
        return false;
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
