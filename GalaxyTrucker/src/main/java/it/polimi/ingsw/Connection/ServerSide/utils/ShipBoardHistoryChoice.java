/*package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

/**
 * Class which handles the command 'previous-shipboard' that let a player
 * visualize a previously printed shipboard. The history can contain 10
 * shipboards maximum for each player.
 *
 * @author carlo

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

public class ShipBoardHistoryChoice {

    public String choose(PlayerMessenger playerMessenger) throws PlayerDisconnectedException {

        ShipBoard[] oldShipBoards = playerMessenger.getLastShipBoards();
        int index = playerMessenger.getLastShipBoardIndex();
        int startingIndex = index;
        index--;

        if(oldShipBoards.length == 0) {
            return "repeat";
        }

        playerMessenger.printMessage("--MOST RECENT--");

        while(true){

            if(index == -1){
                index = 9;
            }

            if(oldShipBoards[index] == null){
                break;
            }

            if(index == startingIndex){
                break;
            }

            playerMessenger.printMessage("ShipBoard " + index);

            index--;

        }

        playerMessenger.printMessage("--LESS RECENT--");

        playerMessenger.printMessage("\nEnter the number of the shipboard you want to select: ");

        String value = playerMessenger.getPlayerString();

        int num;

        if(value.equals("unblock")){
            return "unblocked";
        }

        try {
            num = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            playerMessenger.printMessage("You didn't enter a number!");
            return "repeat";
        }

        if(num >= 0 && num < 10 && num != startingIndex){
            if(oldShipBoards[num] != null){
                playerMessenger.printShipboard(oldShipBoards[num]);
                return "repeat";
            }
        }

        playerMessenger.printMessage("The number you entered is incorrect");
        return "repeat";

    }

}
*/
