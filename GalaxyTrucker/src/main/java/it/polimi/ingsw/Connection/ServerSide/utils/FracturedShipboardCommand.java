package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.List;


public class FracturedShipboardCommand {

    public String showFracturedShipboard(PlayerMessenger playerMessenger) throws PlayerDisconnectedException {

        ShipBoard shipBoard = playerMessenger.getPlayer().getShipBoard();

        List<ShipBoard> fracturedShipboards = shipBoard.getPossibleFracturedShipBoards();

        playerMessenger.printMessage("These are you fractured pieces:\n");

        for(int i = 0; i < fracturedShipboards.size(); i++){
            playerMessenger.printMessage("Fractured piece " + i);
        }

        playerMessenger.printMessage("Select the fractured piece you want to visualize: ");
        String input = playerMessenger.getPlayerString();
        int val;

        if(input.equals("unblock")){
            return "unblocked";
        }

        try{

            val = Integer.parseInt(input);

        } catch (NumberFormatException e) {
            playerMessenger.printMessage("You didn't enter a number!");
            return "repeat";
        }

        if(val >= 0 && val < fracturedShipboards.size()){

            playerMessenger.printShipboard(fracturedShipboards.get(val));

        }
        else{
            playerMessenger.printMessage("The value you entered is incorrect!");
        }

        return "repeat";

    }

}
