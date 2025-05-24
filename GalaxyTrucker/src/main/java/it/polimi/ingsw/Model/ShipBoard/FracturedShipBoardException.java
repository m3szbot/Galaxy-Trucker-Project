package it.polimi.ingsw.Model.ShipBoard;

/**
 * Thrown when the shipboard is broken into separate units.
 */
public class FracturedShipBoardException extends RuntimeException {
    public FracturedShipBoardException(String message) {
        super(message);
    }
}
