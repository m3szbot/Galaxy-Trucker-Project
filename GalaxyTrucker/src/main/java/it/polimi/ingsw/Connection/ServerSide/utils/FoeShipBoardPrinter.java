package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

/**
 * Class which is has the sole purpose of printing the shipboard of another
 * player.
 *
 * @author carlo
 */

public class FoeShipBoardPrinter{

    private int gameCode;
    private PlayerMessenger playerMessenger;

    public FoeShipBoardPrinter(PlayerMessenger playerMessenger){

        this.playerMessenger = playerMessenger;
        this.gameCode = playerMessenger.getGameCode();

    }

    public String start() throws PlayerDisconnectedException{

        Player foe = null;
        boolean playerPresentFlag = false;
        List<Player> connectedPlayers = ClientMessenger.getGameMessenger(gameCode).getConnectedPlayers();

        //he is the only one in game
        if(connectedPlayers.size() == 1){
            playerMessenger.printMessage("Nobody is in game!");
            return "repeat";
        }

        playerMessenger.printMessage("Which player's ship board do you want to spy ?");

        for(Player player: ClientMessenger.getGameMessenger(gameCode).getConnectedPlayers()){

            if(!player.getNickName().equals(playerMessenger.getPlayer().getNickName())) {

                playerMessenger.printMessage(player.getNickName());

            }

        }

        String input = playerMessenger.getPlayerString();

        if(input.equals("unblock")){
            return "unblocked";
        }

        for(Player player: ClientMessenger.getGameMessenger(gameCode).getConnectedPlayers()) {

            if (input.equals(player.getNickName())) {
                foe = player;
                playerPresentFlag = true;
                break;
            }
        }

        if(playerPresentFlag){

           playerMessenger.printMessage(foe.getNickName() + " shipboard");
           playerMessenger.printShipboard(foe.getShipBoard());

       }
       else{

           playerMessenger.printMessage("The player you entered is currently not in game!");

       }

       return "repeat";

    }

}
