package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code ChooseStartingPositionState} handles the state in which a player must choose
 * a starting position on the flight board. It ensures input validation and updates the board.
 */
public class ChooseStartingPositionState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player
    Boolean actionTaken;
    List<Integer> tiles;

    /**
     * Constructs a new {@code ChooseStartingPositionState}.
     *
     * @param assemblyProtocol the shared assembly protocol logic
     * @param playerMessenger  the messenger for communicating with the player
     * @param player           the player who is choosing the starting position
     */
    public ChooseStartingPositionState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }

    /**
     * Called when the player enters this state.
     * Displays the current flight board and prompts the player to choose a starting tile.
     *
     * @param assemblyThread the thread managing the current player's assembly phase
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {
        assemblyThread.getAmIChoosing().set(true);
        actionTaken = false;
        tiles = new ArrayList<>(assemblyProtocol.getFlightBoard().getStartingTiles());
        StringBuilder builder = new StringBuilder();

        // print current flightBoard
        playerMessenger.printFlightBoard(assemblyProtocol.getFlightBoard());
        // get positions message
        builder.append("Choose your starting tile: ");
        for (Integer position : assemblyProtocol.getFlightBoard().getStartingTiles()) {
            builder.append(String.format("%d ", position));
        }
        // print positions message
        playerMessenger.printMessage(builder.toString());
    }

    /**
     * Handles the player's input to select a starting tile.
     * Validates the input and attempts to place the player on the selected tile.
     * On success, transitions to {@code AssemblingEndState}; otherwise, re-prompts the user.
     *
     * @param input           the input string from the player
     * @param assemblyThread  the thread managing the current player's assembly phase
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        if (actionTaken) return;

        int selected;

        actionTaken = true;
        try {
            selected = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            playerMessenger.printMessage("Invalid input: please enter a number.\n");
            assemblyThread.setState(new ChooseStartingPositionState(assemblyProtocol, playerMessenger, player));
            return;
        }

        try {
            try {
                assemblyProtocol.getFlightBoard().addPlayer(player, selected);
                playerMessenger.printFlightBoard(assemblyProtocol.getFlightBoard());
                assemblyThread.setState(new AssemblingEndState(assemblyProtocol, playerMessenger, player));
            }catch (IllegalSelectionException e){
                playerMessenger.printMessage(e.getMessage());
                assemblyThread.setState(new ChooseStartingPositionState(assemblyProtocol, playerMessenger, player));
            }

        } catch (IndexOutOfBoundsException e) {
            // tile selected is unavailable
            playerMessenger.printMessage("The selected starting position is unavailable.\n");
            assemblyThread.setState(new ChooseStartingPositionState(assemblyProtocol, playerMessenger, player));
            return;
        }

    }

}
