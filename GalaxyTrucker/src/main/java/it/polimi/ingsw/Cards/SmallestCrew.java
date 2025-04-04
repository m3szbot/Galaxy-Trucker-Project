package it.polimi.ingsw.Cards;

import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.ArrayList;

/**
 * Interface that defines a method which return
 * the player with the smallest crew members. If there are
 * ties, then the player which is most forward in the flight board
 * is selected.
 *
 * @author carlo
 */

public interface SmallestCrew {

    default Player calculateSmallestCrew(FlightBoard flightBoard, ArrayList<Player> players) {

        int playerPosition = 5, smallestCrew = 100;
        Player smallestCrewPlayer = null;

        for (Player player : players) {

            if (player.getShipBoard().shipBoardAttributes.getCrewMembers() == smallestCrew) {

                if (flightBoard.getPlayerOrder(player) < playerPosition) {

                    playerPosition = flightBoard.getPlayerOrder(player);
                    smallestCrew = player.getShipBoard().shipBoardAttributes.getCrewMembers();
                    smallestCrewPlayer = player;
                }

            } else if (player.getShipBoard().shipBoardAttributes.getCrewMembers() < smallestCrew) {

                playerPosition = flightBoard.getPlayerOrder(player);
                smallestCrew = player.getShipBoard().shipBoardAttributes.getCrewMembers();
                smallestCrewPlayer = player;

            }

        }

        return smallestCrewPlayer;

    }

}
