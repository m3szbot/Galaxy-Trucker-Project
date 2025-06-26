package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;

/**
 * Class which defines a method whose job is to refresh the
 * shipboard of the player entering the command 'refresh'.
 * The command is designed to be used in GUI mode.
 *
 * @author carlo
 */
public class ShipBoardRefresher {

    public String start(PlayerMessenger playerMessenger){

        playerMessenger.printShipboard(playerMessenger.getPlayer().getShipBoard());

        return "repeat";
    }

}
