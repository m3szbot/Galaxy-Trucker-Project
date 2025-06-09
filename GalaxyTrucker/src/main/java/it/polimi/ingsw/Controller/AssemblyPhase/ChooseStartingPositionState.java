package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

public class ChooseStartingPositionState implements GameState {
    AssemblyProtocol assemblyProtocol;
    Player player;
    Boolean actionTaken;
    List<Integer> tiles;

    public ChooseStartingPositionState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    @Override
    public void enter(AssemblyThread assemblyPhase) {
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player);
        assemblyPhase.getAmIChoosing().set(true);
        actionTaken = false;
        tiles = new ArrayList<>(assemblyProtocol.getFlightBoard().getStartingTiles());
        StringBuilder builder = new StringBuilder();

        // print current flightboard
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
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        if (actionTaken) return;

        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player);
        int selected;

        actionTaken = true;
        try {
            selected = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            playerMessenger.printMessage("Invalid input: please enter a number.\n");
            assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
            return;
        }

        try {
            assemblyProtocol.getFlightBoard().addPlayer(player, selected);
            assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));

        } catch (IndexOutOfBoundsException e) {
            // tile selected is unavailable
            playerMessenger.printMessage("The selected starting position is unavailable.\n");
            assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
            return;
        }
        
    }

}
