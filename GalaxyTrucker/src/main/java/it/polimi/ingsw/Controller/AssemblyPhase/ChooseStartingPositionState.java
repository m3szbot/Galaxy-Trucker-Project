package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

public class ChooseStartingPositionState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player
    Boolean actionTaken;
    List<Integer> tiles;

    /**
     * Constructor inherited from GameState.
     */
    public ChooseStartingPositionState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }

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
            assemblyProtocol.getFlightBoard().addPlayer(player, selected);
            assemblyThread.setState(new AssemblingEndState(assemblyProtocol, playerMessenger, player));

        } catch (IndexOutOfBoundsException e) {
            // tile selected is unavailable
            playerMessenger.printMessage("The selected starting position is unavailable.\n");
            assemblyThread.setState(new ChooseStartingPositionState(assemblyProtocol, playerMessenger, player));
            return;
        }

    }

}
