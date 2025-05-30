package it.polimi.ingsw.Model.ShipBoard;

/**
 * Thrown when there are no human crew members left on the shipboard
 * (crewMembers = 0 or only aliens).
 * The player must be immediately disqualified from the flight.
 */
public class NoHumanCrewLeftException extends RuntimeException {
    public NoHumanCrewLeftException() {
        super("No human crew left on shipboard, player forced to give up.");
    }
}
