package it.polimi.ingsw.Model;

/**
 * Checked exception signaling that an inventory is empty and another action must be taken.
 * Must be handled.
 *
 * @author Boti
 */
public class EmptyInventoryException extends Exception {
    public EmptyInventoryException(String message) {
        super(message);
    }
}
