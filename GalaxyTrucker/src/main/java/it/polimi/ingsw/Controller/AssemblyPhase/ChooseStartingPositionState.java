package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.GameMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

public class ChooseStartingPositionState implements GameState{
    AssemblyProtocol assemblyProtocol;
    Player player;
    Boolean actionTaken;

    public ChooseStartingPositionState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    @Override
    public void enter(AssemblyThread assemblyPhase) {
        actionTaken = false;
        List<Integer> tiles  = new ArrayList<>(assemblyProtocol.getFlightBoard().getStartingTiles());
        String message = "In which position do you want to start? (1-4)";
        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
        dataContainer.setMessage(message);
        dataContainer.setCommand("printMessage");
        ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
    }

    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        if(actionTaken) return;
        switch (input.toLowerCase()) {
            case "1":
                actionTaken = true;
                try {assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(1));}
                catch(IndexOutOfBoundsException e){
                    String message = "Error in position choosing, please enter it again";
                    DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                }
                assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                break;
            case "2":
                actionTaken = true;
                if(assemblyProtocol.getFlightBoard().getStartingTiles().size() >= 2 ){
                    try{assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(2));}
                    catch(IndexOutOfBoundsException e){
                        String message = "Error in position choosing, please enter it again";
                        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                }else{
                    String message = "Sorry, there is only one position left";
                    DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
                    assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(1));
                    break;
                }
            case "3":
                actionTaken = true;
                if (assemblyProtocol.getFlightBoard().getStartingTiles().size() >= 3){
                    try{assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(3));}
                    catch (IndexOutOfBoundsException e){
                        String message = "Error in position choosing, please enter it again";
                        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                }else{
                    String message = "Please choose a position between 1 and 2";
                    DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    break;
                }
            case "4":
                actionTaken = true;
                if (assemblyProtocol.getFlightBoard().getStartingTiles().size() == 4){
                    try {assemblyProtocol.getFlightBoard().addPlayer(player, assemblyProtocol.getFlightBoard().getStartingTiles().get(4));}
                    catch (IndexOutOfBoundsException e){
                        String message = "Error in position choosing, please enter it again";
                        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
                        assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    }
                    assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
                    break;
                }else{
                    String message = "Please choose a position between 1 and 3";
                    DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
                    assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                    break;
                }
            default:
                actionTaken = true;
                String message = "Invalid input";
                DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerData(player);
                assemblyPhase.setState(new ChooseStartingPositionState(assemblyProtocol, player));
                break;
        }
        assemblyPhase.end.set(assemblyPhase.end.get() + 1);
    }
}
