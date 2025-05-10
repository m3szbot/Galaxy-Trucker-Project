package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.socket.ClientSocketMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

public class ChooseStartingPositionState implements GameState{
    AssemblyProtocol assemblyProtocol;
    Player player;

    public ChooseStartingPositionState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    @Override
    public void enter(AssemblyThread assemblyPhase) {
        assemblyPhase.isfinished = true;
        List<Integer> tiles  = new ArrayList<>(assemblyProtocol.getFlightBoard().getStartingTiles());
        String message = "In which position do you want to start? (1-4)";
        ClientSocketMessenger.sendMessageToPlayer(player, message);
    }

    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        switch (input.toLowerCase()) {
            case "1":
                try {assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(1));}
                catch(IndexOutOfBoundsException e){
                    String message = "Error in position choosing, please enter it again";
                    ClientSocketMessenger.sendMessageToPlayer(player, message);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                }
                assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                break;
            case "2":
                if(assemblyProtocol.getFlightBoard().getStartingTiles().size() >= 2 ){
                    try{assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(2));}
                    catch(IndexOutOfBoundsException e){
                        String message = "Error in position choosing, please enter it again";
                        ClientSocketMessenger.sendMessageToPlayer(player, message);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                }else{
                    String message = "Sorry, there is only one position left";
                    ClientSocketMessenger.sendMessageToPlayer(player, message);
                    assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(1));
                    break;
                }
            case "3":
                if (assemblyProtocol.getFlightBoard().getStartingTiles().size() >= 3){
                    try{assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(3));}
                    catch (IndexOutOfBoundsException e){
                        String message = "Error in position choosing, please enter it again";
                        ClientSocketMessenger.sendMessageToPlayer(player, message);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                }else{
                    String message = "Please choose a position between 1 and 2";
                    ClientSocketMessenger.sendMessageToPlayer(player, message);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    break;
                }
            case "4":
                if (assemblyProtocol.getFlightBoard().getStartingTiles().size() == 4){
                    try {assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(4));}
                    catch (IndexOutOfBoundsException e){
                        String message = "Error in position choosing, please enter it again";
                        ClientSocketMessenger.sendMessageToPlayer(player, message);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                }else{
                    String message = "Please choose a position between 1 and 3";
                    ClientSocketMessenger.sendMessageToPlayer(player, message);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    break;
                }
            default:
                String message = "Invalid input";
                ClientSocketMessenger.sendMessageToPlayer(player, message);
                assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                break;
        }
        assemblyPhase.end.set(assemblyPhase.end.get() + 1);
    }
}
