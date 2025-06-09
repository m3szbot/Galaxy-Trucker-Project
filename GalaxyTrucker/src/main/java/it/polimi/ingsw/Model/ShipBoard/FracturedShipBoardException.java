package it.polimi.ingsw.Model.ShipBoard;

import java.util.List;

/**
 * Thrown when the shipboard is broken into separate parts.
 * Must be handled by caller,
 * by calling FracturedShipboardHandler.handleFracture().
 */
public class FracturedShipBoardException extends Exception {
    List<ShipBoard> validShipBoardsList;

    public FracturedShipBoardException(List<ShipBoard> validShipBoardsList) {
        this.validShipBoardsList = validShipBoardsList;
    }

    public List<ShipBoard> getValidShipBoardsList() {
        return validShipBoardsList;
    }
}
