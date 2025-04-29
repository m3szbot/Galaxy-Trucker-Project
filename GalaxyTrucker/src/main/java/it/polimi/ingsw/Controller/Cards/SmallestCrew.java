package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Interface that defines a default method which return
 * the player with the smallest crew members. If there are
 * ties, then the player which is most forward in the flight board
 * is selected.
 *
 * @author carlo
 */

public interface SmallestCrew {

    /**
     *
     * @param flightBoard flightboard of the current game
     * @return the player with the smallest number of inhabitants on the ship
     *
     * @author Carlo
     */

    default Player calculateSmallestCrew(FlightBoard flightBoard) {

        int smallestCrew = 100;
        Player smallestCrewPlayer = null;

        for (Player player : flightBoard.getPlayerOrderList()) {

            if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() < smallestCrew) {

                smallestCrew = player.getShipBoard().getShipBoardAttributes().getCrewMembers();
                smallestCrewPlayer = player;

            }

        }

        return smallestCrewPlayer;

    }

}
