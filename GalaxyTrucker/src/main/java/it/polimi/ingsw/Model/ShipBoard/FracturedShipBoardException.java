package it.polimi.ingsw.Model.ShipBoard;

import java.util.List;

/**
 * Thrown when the shipboard is broken into separate units.
 * To be handled by FracturedShipBoardHandler.
 */
public class FracturedShipBoardException extends RuntimeException {
    Player player;
    List<ShipBoard> shipBoardsList;

    public FracturedShipBoardException(List<ShipBoard> shipBoardsList) {
        super("Exception: the shipboard is fractured into more pieces.");
        this.shipBoardsList = shipBoardsList;
    }

    public Player getPlayer() {
        return player;
    }

    public List<ShipBoard> getShipBoardsList() {
        return shipBoardsList;
    }
}
