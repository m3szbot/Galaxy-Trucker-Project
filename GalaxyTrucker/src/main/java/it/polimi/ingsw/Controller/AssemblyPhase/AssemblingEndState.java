package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.socket.ClientSocketMessenger;
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
        ClientSocketMessenger.sendMessageToPlayer(player, message);
    }

    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
       switch (input.toLowerCase()) {
           case "yes":
               if(assemblyProtocol.getHourGlass().isFinished() == true){
                   assemblyProtocol.getHourGlass().twist();
               }
               else{
                   String message = "HourGlass is already running";
                   ClientSocketMessenger.sendMessageToPlayer(player, message);
                   assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
               }
               break;
           default:
               String message = "Invalid input";
               ClientSocketMessenger.sendMessageToPlayer(player, message);
               assemblyPhase.setState(new AssemblingEndState(assemblyProtocol, player));
               break;
       }
    }
}
