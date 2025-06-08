package it.polimi.ingsw.Model.ShipBoard;

/**
 * Thrown when there are no human crew members left on the shipboard.
 * The player must be immediately forced to give up in the flight.
 * Must be handled by caller,
 * by calling Phase.forcePlayerToGiveUp().
 */
public class NoHumanCrewLeftException extends Exception {
    public NoHumanCrewLeftException() {
        super("No human crew left on shipboard, player forced to give up.");
    }
}
