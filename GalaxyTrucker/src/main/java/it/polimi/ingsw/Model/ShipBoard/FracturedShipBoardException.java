package it.polimi.ingsw.Model.ShipBoard;

import java.util.List;

/**
 * Thrown when the shipboard is broken into separate parts.
 * Must be handled by caller,
 * by calling FracturedShipboardHandler.start().
 */
public class FracturedShipBoardException extends Exception {
    List<ShipBoard> shipBoardsList;

    public FracturedShipBoardException(List<ShipBoard> shipBoardsList) {
        this.shipBoardsList = shipBoardsList;
    }

    public List<ShipBoard> getShipBoardsList() {
        return shipBoardsList;
    }
}
