package it.polimi.ingsw.Model.ShipBoard;

/**
 * Thrown when there are no human crew members left on the shipboard.
 * The player must be immediately forced to give up in the flight.
 * Must be handled by caller.
 */
public class NoHumanCrewLeftException extends Exception {

    public NoHumanCrewLeftException() {
    }

}
