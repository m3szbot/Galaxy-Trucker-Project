package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

public class ChooseStartingPositionState implements GameState {
    AssemblyProtocol assemblyProtocol;
    Player player;
    Boolean actionTaken;

    public ChooseStartingPositionState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    @Override
    public void enter(AssemblyThread assemblyPhase) {
        assemblyPhase.amIChoosing.set(true);
        actionTaken = false;
        List<Integer> tiles = new ArrayList<>(assemblyProtocol.getFlightBoard().getStartingTiles());
        String message = "In which position do you want to start? (1-4)";
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
    }

    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        if (actionTaken) return;
        switch (input.toLowerCase()) {
            case "1":
                actionTaken = true;
                try {
                    assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(3)); //3
                } catch (IndexOutOfBoundsException e) {
                    String message = "Error in position choosing, please enter it again";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                }
                assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                break;
            case "2":
                actionTaken = true;
                if (assemblyProtocol.getFlightBoard().getStartingTiles().size() >= 2) {
                    try {
                        assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(2)); //2
                    } catch (IndexOutOfBoundsException e) {
                        String message = "Error in position choosing, please enter it again";
                        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                } else {
                    String message = "Sorry, there is only one position left";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                    assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(1));
                    break;
                }
            case "3":
                actionTaken = true;
                if (assemblyProtocol.getFlightBoard().getStartingTiles().size() >= 3) {
                    try {
                        assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(1)); //1
                    } catch (IndexOutOfBoundsException e) {
                        String message = "Error in position choosing, please enter it again";
                        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                } else {
                    String message = "Please choose a position between 1 and 2";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    break;
                }
            case "4":
                actionTaken = true;
                if (assemblyProtocol.getFlightBoard().getStartingTiles().size() == 4) {
                    try {
                        assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(0)); //0
                    } catch (IndexOutOfBoundsException e) {
                        String message = "Error in position choosing, please enter it again";
                        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                } else {
                    String message = "Please choose a position between 1 and 3";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    break;
                }
            default:
                actionTaken = true;
                String message = "Invalid input";
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                break;
        }
    }
}
