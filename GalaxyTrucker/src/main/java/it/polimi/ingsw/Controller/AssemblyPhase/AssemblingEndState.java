package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class AssemblingEndState implements GameState {
    AssemblyProtocol assemblyProtocol;
    Player player;

    public AssemblingEndState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    @Override
    public void enter(AssemblyThread assemblyPhase) {
        String message = "Do you want to turn the hourglass? (yes or wait)";
        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
        dataContainer.setMessage(message);
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
    }

    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
       switch (input.toLowerCase()) {
           case "yes":
               if(assemblyProtocol.getHourGlass().isFinished() == true){
                   assemblyProtocol.getHourGlass().twist(assemblyProtocol, ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerSocketMap().keySet().stream().toList() );
               }
               else{
                   String message = "HourGlass is already running";
                   DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                   dataContainer.setMessage(message);
                   ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                   assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
               }
               break;
           default:
               String message = "Invalid input";
               DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
               dataContainer.setMessage(message);
               ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
               assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
               break;
       }
    }
}
