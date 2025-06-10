package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Connection.ServerSide.utils.CommandHandler;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Input thread of flight phase
 *
 * @author carlo
 */

public class InputThread extends Thread{

    private PlayerMessenger playerMessenger;
    private Player player;
    private GameInformation gameInformation;
    private volatile boolean running;
    private volatile boolean isPlayerTurn;
    private boolean blocked;

    public InputThread(Player player, GameInformation gameInformation){
        this.playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        this.running = true;
        this.isPlayerTurn = false;
        this.player = player;
        this.gameInformation = gameInformation;
    }

    @Override
    public void run() {

           while(running){

               try {

                   blocked = true;
                   String input = playerMessenger.getPlayerString();
                   blocked = false;

                   CommandHandler.executeCommand(input, playerMessenger);

                   //It's the player turn
                   while (isPlayerTurn){
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           System.err.println("Input thread of flight phase was interrupted abnormally");
                       }
                   }


               } catch (PlayerDisconnectedException e) {

                   ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);

               }

           }
    }

   public void setPlayerTurn(boolean val){
        this.isPlayerTurn = val;

        if(val){
            if(blocked) {
                playerMessenger.unblockUserInputGetterCall();
            }

        }
   }

   public void endThread(){

        this.running = false;
        if(isPlayerTurn){
            isPlayerTurn = false;
        }
        else{
            if(blocked) {
                playerMessenger.unblockUserInputGetterCall();
            }
        }

   }
}
