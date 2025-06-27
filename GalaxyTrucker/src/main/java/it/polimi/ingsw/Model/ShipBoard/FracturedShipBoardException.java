package it.polimi.ingsw.Model.ShipBoard;

import java.util.List;

/**
 * Thrown when the shipboard is broken into separate parts.
 * Must be handled by caller,
 * by calling FracturedShipboardHandler.handleFracturedShipBoardException().
 */
public class FracturedShipBoardException extends Exception {
    List<ShipBoard> validShipBoardsList;

    public FracturedShipBoardException(List<ShipBoard> validShipBoardsList) {
        // throw exception if exception is incorrectly thrown
        if (validShipBoardsList.size() <= 1)
            throw new IllegalArgumentException("Shipboard is not fractured, shouldn't throw fractured exception");

        this.validShipBoardsList = validShipBoardsList;
    }

    public List<ShipBoard> getValidShipBoardsList() {
        return validShipBoardsList;
    }
}
