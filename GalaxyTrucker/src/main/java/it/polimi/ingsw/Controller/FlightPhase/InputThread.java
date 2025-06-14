package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Connection.ServerSide.utils.CommandHandler;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Input thread of flight phase
 *
 * @author carlo
 */

public class InputThread extends Thread{

    private PlayerMessenger playerMessenger;
    private Player player;
    private GameInformation gameInformation;
    private AtomicBoolean running;
    private AtomicBoolean isPlayerTurn;
    private AtomicBoolean blocked;
    private AtomicBoolean commandExecuting;

    public InputThread(Player player, GameInformation gameInformation){
        this.playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        this.running = new AtomicBoolean(true);
        this.isPlayerTurn = new AtomicBoolean(false);
        this.player = player;
        this.gameInformation = gameInformation;
        this.commandExecuting = new AtomicBoolean(false);
        this.blocked = new AtomicBoolean(false);
    }

    @Override
    public void run() {

           while(running.get()){

               try {

                   blocked.set(true);
                   String input = playerMessenger.getPlayerString();
                   blocked.set(false);

                   commandExecuting.set(true);
                   CommandHandler.executeCommand(input, playerMessenger);
                   commandExecuting.set(false);

                   //It's the player turn
                   while (isPlayerTurn.get()){
                       Sleeper.sleepXSeconds(1);
                   }


               } catch (PlayerDisconnectedException e) {

                   ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                   this.running.set(false);

               }

           }
    }

   public void setPlayerTurn(boolean val){
        this.isPlayerTurn.set(val);

        if(val){

            if(commandExecuting.get()){
                playerMessenger.unblockUserInputGetterCall();
            }

            if(blocked.get()) {
                playerMessenger.unblockUserInputGetterCall();
            }

        }
   }

   public void endThread(){

        this.running.set(false);
        isPlayerTurn.set(false);

        if(blocked.get() || commandExecuting.get()){
            playerMessenger.unblockUserInputGetterCall();
        }

   }
}
