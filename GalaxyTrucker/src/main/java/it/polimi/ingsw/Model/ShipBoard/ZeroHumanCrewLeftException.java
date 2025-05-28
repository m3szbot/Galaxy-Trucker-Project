package it.polimi.ingsw.Model.ShipBoard;

/**
 * Thrown when there are no human crew members left on the shipboard
 * (crewMembers = 0 or only aliens).
 * The player must be immediately disqualified from the flight.
 */
public class ZeroHumanCrewLeftException extends RuntimeException {
    public ZeroHumanCrewLeftException(String message) {
        super(message);
    }
}
