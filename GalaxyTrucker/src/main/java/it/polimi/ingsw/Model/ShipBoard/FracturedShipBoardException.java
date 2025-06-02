package it.polimi.ingsw.Model.ShipBoard;

import java.util.List;

/**
 * Thrown when the shipboard is broken into separate units.
 * To be handled by FracturedShipBoardHandler.
 */
public class FracturedShipBoardException extends RuntimeException {
    List<ShipBoard> shipBoardsList;

    public FracturedShipBoardException(List<ShipBoard> shipBoardsList) {
        this.shipBoardsList = shipBoardsList;
    }

    public List<ShipBoard> getShipBoardsList() {
        return shipBoardsList;
    }
}
