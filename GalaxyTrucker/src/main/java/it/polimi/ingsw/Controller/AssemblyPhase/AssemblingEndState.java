package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.GameInformation.GameType;
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
        assemblyPhase.isfinished.set(true);
        if(!assemblyPhase.running.get()){
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerMessage(player, "Waiting for other players position choice");
            assemblyPhase.end.set(true);
            assemblyPhase.latch.countDown();
            return;
        }
        String message = "Do you want to turn the hourglass? (yes or wait)";
        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
        dataContainer.setMessage(message);
        dataContainer.setCommand("printMessage");
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
    }

    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
       switch (input.toLowerCase()) {
           case "yes":
               if(assemblyProtocol.getHourGlass().isFinished() == true) {
                   if (assemblyPhase.gameInformation.getGameType().equals(GameType.NORMALGAME)) {
                       if (assemblyProtocol.getHourGlass().getState() == 2) {
                           ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerMessage(player, "Waiting for other players position choice");
                           assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyPhase.gameInformation.getPlayerList());
                           assemblyPhase.latch.countDown();
                       } else {
                           assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyPhase.gameInformation.getPlayerList());
                       }
                   }else{
                       if (assemblyProtocol.getHourGlass().getState() == 2){
                           ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerMessage(player, "Waiting for other players position choice");
                           assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyPhase.gameInformation.getPlayerList());
                           assemblyPhase.latch.countDown();
                       }else{
                           assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyPhase.gameInformation.getPlayerList());
                       }
                   }
               }
               else{
                   String message = "HourGlass is already running";
                   DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                   dataContainer.setMessage(message);
                   dataContainer.setCommand("printMessage");
                   ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
               }
               assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
               break;
           default:
               String message = "Invalid input";
               DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
               dataContainer.setMessage(message);
               dataContainer.setCommand("printMessage");
               ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
               assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
               break;
       }
    }
}
