package it.polimi.ingsw.cards;

import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.shipboard.Player;

/**
 * Interface that defines a method which move the player
 * position on the flight board
 *
 * @author carlo
 */

public interface Movable {

    default void changePlayerPosition(Player player, int numberOfDays, FlightBoard flightBoard){

        flightBoard.incrementPlayerTile(player, numberOfDays);

    }

}
