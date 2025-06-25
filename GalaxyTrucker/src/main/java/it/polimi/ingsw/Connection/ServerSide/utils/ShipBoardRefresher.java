package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;

/**
 * Class which defines a method which job is to refresh the
 * shipboard of the player entering the command 'refresh'
 *
 * @author carlo
 */
public class ShipBoardRefresher {

    public String start(PlayerMessenger playerMessenger){

        playerMessenger.printShipboard(playerMessenger.getPlayer().getShipBoard());

        return "repeat";
    }

}
