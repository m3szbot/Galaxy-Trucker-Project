package it.polimi.ingsw.Model.FlightBoard;

import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Signals that the player has been lapped on the flightBoard and must be eliminated.
 *
 * @author Boti
 */
public class LappedPlayersException extends Exception {
    FlightBoard flightBoard;
    List<Player> playerList;

    public LappedPlayersException(FlightBoard flightBoard, List<Player> playerList) {
        // throw exception if exception is incorrectly thrown
        if (playerList.isEmpty())
            throw new IllegalArgumentException("Error: no players have been lapped, shouldn't throw lapped exception.");

        // construct exception
        this.flightBoard = flightBoard;
        this.playerList = new ArrayList<>(playerList);
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

}
