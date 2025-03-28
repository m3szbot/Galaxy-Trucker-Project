package it.polimi.ingsw.cards;

import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.shipboard.Player;

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

    default Player calculateSmallestCrew(FlightBoard flightBoard, ArrayList<Player> players){

        int playerPosition = 5, smallestCrew = 100;
        Player smallestCrewPlayer = null;

        for(Player player: players){

            if(player.shipStructure.shipBoard.getCrewMembers() == smallestCrew){

                if(flightBoard.getPlayerOrder(player) < playerPosition){

                    playerPosition = flightBoard.getPlayerOrder(player);
                    smallestCrew = player.shipStructure.shipBoard.getCrewMembers();
                    smallestCrewPlayer = player;
                }

            }
            else if(player.shipStructure.shipBoard.getCrewMembers() < smallestCrew){

                playerPosition = flightBoard.getPlayerOrder(player);
                smallestCrew = player.shipStructure.shipBoard.getCrewMembers();
                smallestCrewPlayer = player;

            }

        }

        return smallestCrewPlayer;

    }

}
