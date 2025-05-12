package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Model.ShipBoard.Player;

public class PlayerDisconnectedException extends Exception{

    public PlayerDisconnectedException(Player player){
        super("Player " + player.getNickName() + " disconnected!");
    }
}
