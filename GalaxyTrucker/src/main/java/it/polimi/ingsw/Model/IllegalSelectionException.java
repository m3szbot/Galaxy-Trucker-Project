package it.polimi.ingsw.Model;

/**
 * Exception that signals an illegal player selection.
 */
public class IllegalSelectionException extends Exception {
    public IllegalSelectionException(String message) {
        super(message);
    }
}
