package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class AssemblingEndState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;

    public AssemblingEndState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    @Override
    public void enter(AssemblyThread assemblyPhase) {
        assemblyPhase.getIsfinished().set(true);
        if(assemblyProtocol.getFlightBoard().getPlayerOrderList().size() == assemblyPhase.getGameInformation().getPlayerList().size()){
            assemblyPhase.setRunning(false);
            assemblyPhase.setEnd();
            return;
        }
        if(!assemblyPhase.getRunning().get()){
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage("Waiting for other players position choice");
            //assemblyPhase.end.set(true);
            //return;
        }else{
            String message = "Do you want to turn the hourglass? (write ---> yes <---, or wait for other players to complete their shipboard)";
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
        }
    }

    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {

       switch (input.toLowerCase()) {
           case "yes":
               if(assemblyProtocol.getHourGlass().isFinished() == true) {
                   if (assemblyPhase.getGameInformation().getGameType().equals(GameType.NORMALGAME)) {
                       if (assemblyProtocol.getHourGlass().getState() == 2) {
                           ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage("Waiting for other players position choice");
                           assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyPhase.getGameInformation().getPlayerList());
                       } else {
                           assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyPhase.getGameInformation().getPlayerList());
                       }
                   }else{
                       if (assemblyProtocol.getHourGlass().getState() == 1){
                           ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage("Waiting for other players position choice");
                           assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyPhase.getGameInformation().getPlayerList());
                       }else{
                           assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyPhase.getGameInformation().getPlayerList());
                       }
                   }
               }
               else{
                   String message = "HourGlass is already running";
                   ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
               }
               assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
               break;
           default:
               String message = "Invalid input";
               ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
               assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
               break;
       }
    }
}
