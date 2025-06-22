package it.polimi.ingsw.Controller.FlightPhase.Cards;

import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
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
     * @param gameInformation flight board of the current game
     * @return the player with the smallest number of inhabitants on the ship
     * @author Carlo
     */

    default Player calculateSmallestCrew(GameInformation gameInformation) {

        int smallestCrew = gameInformation.getFlightBoard().getPlayerOrderList().getLast().getShipBoard().getShipBoardAttributes().getCrewMembers();
        Player smallestCrewPlayer = null, player;

        for (int i = gameInformation.getFlightBoard().getPlayerOrderList().size() - 1; i >= 0; i--) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);

            //Searches for the player with the smallest crew, if there's more than one it selects the first on the list
            if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() <= smallestCrew) {

                smallestCrew = player.getShipBoard().getShipBoardAttributes().getCrewMembers();
                smallestCrewPlayer = player;

            }

        }

        return smallestCrewPlayer;

    }

}
