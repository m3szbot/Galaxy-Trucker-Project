package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Signals that the player has been disconnected from the server.
 * Must be handled by caller,
 * by calling gameMessenger.disconnectPlayer().
 */
public class PlayerDisconnectedException extends Exception {

    public PlayerDisconnectedException(Player player) {
        super("Player " + player.getNickName() + " disconnected!");
    }
    public PlayerDisconnectedException(String nickname){
        super("Player " + nickname + " disconnected!");
    }
}