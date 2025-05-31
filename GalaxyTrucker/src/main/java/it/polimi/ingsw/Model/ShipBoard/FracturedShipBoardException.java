package it.polimi.ingsw.Model.ShipBoard;

import java.util.List;

/**
 * Thrown when the shipboard is broken into separate units.
 * To be handled by FracturedShipBoardHandler.
 */
public class FracturedShipBoardException extends RuntimeException {
    Player player;
    List<ShipBoard> shipBoardsList;

    public FracturedShipBoardException(String message, Player player, List<ShipBoard> shipBoardsList) {
        super(message);
        this.player = player;
        this.shipBoardsList = shipBoardsList;
    }

    public Player getPlayer() {
        return player;
    }

    public List<ShipBoard> getShipBoardsList() {
        return shipBoardsList;
    }
}
